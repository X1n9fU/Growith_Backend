package dev.book.user_friend.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import dev.book.global.config.security.dto.CustomUserDetails;
import dev.book.global.config.security.jwt.JwtAuthenticationToken;
import dev.book.user.entity.UserEntity;
import dev.book.user.repository.UserRepository;
import dev.book.user_friend.dto.EncryptUserInfo;
import dev.book.user_friend.dto.response.KakaoResponseDto;
import dev.book.user_friend.entity.UserFriend;
import dev.book.user_friend.repository.UserFriendRepository;
import dev.book.user_friend.util.AESUtil;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.StringContains.containsString;


import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class UserFriendControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserFriendRepository userFriendRepository;

    CustomUserDetails userDetails;

    String invitingUserToken;

    @BeforeEach
    public void mockMvcSetUp(){
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @BeforeEach
    public void createUser(){
        UserEntity user = userRepository.save(UserEntity.builder()
                .email("test@test.com")
                .name("test")
                .nickname("nickname")
                .profileImageUrl("profile").build());
        userDetails = new CustomUserDetails(user);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));

        UserEntity user2 = userRepository.save(UserEntity.builder()
                .email("test2@test.com")
                .name("test")
                .nickname("nickname")
                .profileImageUrl("profile").build());
        userDetails = new CustomUserDetails(user2);
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));

    }

    @AfterEach
    public void deleteUser(){
        userFriendRepository.deleteAll();
        userRepository.deleteAll();
    }

    //test ) test@test.com 으로 토큰 생성
    private String getToken() throws Exception{

        Optional<UserEntity> user = userRepository.findByEmail("test@test.com");
        userDetails = new CustomUserDetails(user.get());
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));

        final String url = "/api/v1/friends/invite/token";

        final ResultActions actions = mockMvc.perform(get(url));
        MvcResult result = actions.andReturn();

        String json = result.getResponse().getContentAsString();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = objectMapper.readTree(json);
        invitingUserToken = root.get("invitingUserToken").asText();
        return invitingUserToken;
    }

    //test ) 초대 요청 내역 생성
    private String makeInvitation() throws Exception {
        //test@test.com 으로부터 초대 요청 생성
        String token = getToken();
        final ResultActions actions = mockMvc.perform(post("/api/v1/friends/kakao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new KakaoResponseDto("chat_type", 10000L, "hash_chat_id", token))));
        return token;
    }

    //test2 ) 초대 토큰을 포함한 링크로 접속, 초대 요청 내역 완성
    private String completeInvitation() throws Exception {
        String token = makeInvitation();

        Optional<UserEntity> user = userRepository.findByEmail("test2@test.com");
        userDetails = new CustomUserDetails(user.get());
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(userDetails, userDetails.getAuthorities()));

        //초대 요청 생성
        mockMvc.perform(get("/api/v1/friends/invite?token="+token))
                .andExpect(status().is(302))
                .andExpect(header().string("Location", containsString("/main")));

        return token;
    }

    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("getInviteUserToken: 친구 초대 토큰을 발급합니다.")
    void getInviteUserToken() throws Exception {

        final String url = "/api/v1/friends/invite/token";

        final ResultActions actions = mockMvc.perform(get(url));

        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$.invitingUserToken").isNotEmpty());
    }


    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("kakao에서 보낸 웹 훅을 받아 토큰으로 친구 요청을 구성한다.")
    void getWebHookFromKakao() throws Exception {

        String invitingUserToken = getToken();
        final String url = "/api/v1/friends/kakao";

        final ResultActions actions = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new KakaoResponseDto("chat_type", 10000L, "hash_chat_id", invitingUserToken))));

        actions.andExpect(status().isOk());

        EncryptUserInfo info = getEncryptUserInfo(invitingUserToken);

        Optional<UserFriend> userFriend = userFriendRepository.findByInvitingUserAndRequestedAt(info.id(), info.localDateTime());

        assertThat(userFriend).isNotEmpty();
        assertThat(userFriend.get().getFriend()).isNull();
    }

    //암호화된 유저 정보 복호화
    private EncryptUserInfo getEncryptUserInfo(String invitingUserToken) throws Exception {
        String json = AESUtil.decrypt(URLDecoder.decode(invitingUserToken, StandardCharsets.UTF_8));
        objectMapper.registerModule(new JavaTimeModule());
        EncryptUserInfo info = objectMapper.readValue(json, EncryptUserInfo.class);
        return info;
    }

    @Test
    @WithMockUser(username = "test2@test.com")
    @DisplayName("getTokenAndMakeInvitation : URL에 포함된 토큰을 갖고 친구 요청 완성")
    void getTokenAndMakeInvitation() throws Exception {

        String token = completeInvitation();

        EncryptUserInfo userInfo = getEncryptUserInfo(token);

        Optional<UserEntity> user = userRepository.findByEmail(userInfo.email());
        Optional<UserEntity> friend = userRepository.findByEmail(userDetails.getUsername());
        Optional<UserFriend> userFriend = userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(user.get(), friend.get());

        assertThat(userFriend).isNotEmpty();
        assertThat(userFriend.get().getFriend()).isNotNull();
    }

    @Test
    @WithMockUser(username = "test2@test.com")
    void getFriendList() throws Exception {

        String token = completeInvitation();

        EncryptUserInfo info = getEncryptUserInfo(token);

        //친구 요청 받기
        mockMvc.perform(get("/api/v1/friends/request/"+info.id()+"/accept"))
                .andExpect(status().isOk());

        final String url = "/api/v1/friends/list";

        mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].friendUserId").value(info.id()));


        //test(유저) -> test2(친구) 의 요청을 test2가 확인
        Optional<UserEntity> user;
        user = userRepository.findByEmail(info.email());

        //서로에게 친구가 보여지는 것 확인
        List<UserEntity> list;
        list = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(user.get().getId());
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getEmail()).isEqualTo(userDetails.getUsername());

        list = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(userDetails.user().getId());
        assertThat(list).isNotEmpty();
        assertThat(list.get(0).getEmail()).isEqualTo(user.get().getEmail());


    }

    @Test
    @WithMockUser(username = "test@test.com")
    @DisplayName("getFriendRequestList : 친구 요청 목록 반환")
    void getFriendRequestList() throws Exception {
        completeInvitation();

        //test2에게 test -> test2의 요청은 보임
        List<UserEntity> userFriends;
        userFriends = userFriendRepository.findAllByInvitedUserAndIsRequestIsTrue(userDetails.user().getId());

        assertThat(userFriends).isNotEmpty();
        assertThat(userFriends.get(0).getEmail()).isEqualTo("test@test.com");

        //test1에게는 요청이 보이지 않음
        Optional<UserEntity> invitingUser = userRepository.findByEmail("test@test.com");
        userFriends = userFriendRepository.findAllByInvitedUserAndIsRequestIsTrue(invitingUser.get().getId());
        assertThat(userFriends).isEmpty();
    }


    @Test
    @WithMockUser(username = "test2@test.com")
    @DisplayName("acceptFriend: 친구 요청 승낙")
    void acceptFriend() throws Exception {

        String token = completeInvitation();
        EncryptUserInfo info = getEncryptUserInfo(token);

        //test(유저) -> test2(친구) 의 요청을 test2가 확인
        Optional<UserEntity> user;
        user = userRepository.findByEmail(info.email());
        Optional<UserFriend> userFriend;
        userFriend = userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(user.get(), userDetails.user());

        assertThat(userFriend).isNotEmpty();
        assertThat(userFriend.get().getIsAccept()).isFalse(); //아직 받지 않은 상태

        final String url = "/api/v1/friends/request/"+info.id()+"/accept";

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        user = userRepository.findByEmail(info.email());
        userFriend = userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(user.get(), userDetails.user());

        assertThat(userFriend).isEmpty(); //친구 요청을 승낙해서 요청이 없음

        //서로가 친구로 되어 있는 모습
        List<UserEntity> userEntities;
        userEntities = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(userDetails.user().getId());
        assertThat(userEntities).isNotEmpty();
        assertThat(userEntities.get(0).getEmail()).isEqualTo(user.get().getEmail());

        userEntities = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(user.get().getId());
        assertThat(userEntities).isNotEmpty();
        assertThat(userEntities.get(0).getEmail()).isEqualTo(userDetails.user().getEmail());
    }

    @Test
    @WithMockUser(username = "test2@test.com")
    @DisplayName("rejectFriend: 친구 요청 거절")
    void rejectFriend() throws Exception {
        String token = completeInvitation();
        EncryptUserInfo info = getEncryptUserInfo(token);

        //test(유저) -> test2(친구) 의 요청을 test2가 확인
        Optional<UserEntity> user;
        user = userRepository.findByEmail(info.email());
        Optional<UserFriend> userFriend;
        userFriend = userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(user.get(), userDetails.user());

        assertThat(userFriend).isNotEmpty();
        assertThat(userFriend.get().getIsAccept()).isFalse(); //아직 받지 않은 상태

        final String url = "/api/v1/friends/request/"+info.id()+"/reject";

        mockMvc.perform(get(url))
                .andExpect(status().isOk());

        //요청 지워짐
        userFriend = userFriendRepository.findByUserAndFriendAndIsRequestIsTrue(user.get(), userDetails.user());
        assertThat(userFriend).isEmpty();

        //받은 상태가 아니기에 비어있음
        List<UserEntity> userEntities;
        userEntities = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(userDetails.user().getId());
        assertThat(userEntities).isEmpty();

        userEntities = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(user.get().getId());
        assertThat(userEntities).isEmpty();

    }

    @Test
    @WithMockUser(username = "test2@test.com")
    @DisplayName("deleteFriend: 친구를 삭제합니다.")
    void deleteFriend() throws Exception {
        Optional<UserEntity> user = userRepository.findByEmail("test@test.com");
        Optional<UserEntity> user2 = userRepository.findByEmail(userDetails.getUsername());
        UserFriend userFriend = UserFriend.builder().user(user.get()).friend(user2.get()).build();
        userFriendRepository.save(userFriend);

        final String url = "/api/v1/friends/"+user.get().getId();

        mockMvc.perform(delete(url));

        //양쪽에서 친구를 조회했을 때 아무것도 반환이 되지 않음
        List<UserEntity> users;
        users = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(user.get().getId());
        assertThat(users).isEmpty();

        users = userFriendRepository.findAllByInvitedUserAndIsAcceptIsTrue(userDetails.user().getId());
        assertThat(users).isEmpty();

    }

}