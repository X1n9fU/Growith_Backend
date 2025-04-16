package dev.book.challenge.service;

import dev.book.challenge.dto.request.ChallengeCreateRequest;
import dev.book.challenge.dto.request.ChallengeUpdateRequest;
import dev.book.challenge.dto.response.ChallengeCreateResponse;
import dev.book.challenge.dto.response.ChallengeReadDetailResponse;
import dev.book.challenge.dto.response.ChallengeReadResponse;
import dev.book.challenge.dto.response.ChallengeUpdateResponse;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.user.entity.UserEntity;
import dev.book.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ChallengeServiceTest {

    @InjectMocks
    private ChallengeService challengeService;

    @Mock
    private ChallengeRepository challengeRepository;

    @Mock
    private UserChallengeRepository userChallengeRepository;

    @Mock
    private UserRepository userRepository;

    private ChallengeCreateRequest createRequest() {
        LocalDate start = LocalDate.of(2024, 1, 1);
        LocalDate end = LocalDate.of(2024, 2, 1);
        return new ChallengeCreateRequest("제목", "내용", "PUBLIC", 1000, 5, "NONE", start, end);
    }

    @Test
    @DisplayName("챌린지를 등록할수 있다.")
    void createChallenge() {

        // given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("사용자")
                .email("이메일")
                .build();
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        given(challengeRepository.save(any())).willReturn(challenge);

        // when
        ChallengeCreateResponse response = challengeService.createChallenge(creator, challengeCreateRequest);
        //then
        assertThat(response.title()).isEqualTo("제목");


    }

    @Test
    @DisplayName("챌린지를 조회 할수 있다.")
    void searchChallenge() {

        // given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("사용자")
                .build();
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        ChallengeReadResponse challengeReadResponse = ChallengeReadResponse.fromEntity(challenge);
        Pageable pageRequest = PageRequest.of(0, 10);
        Page<ChallengeReadResponse> mockPage = new PageImpl<>(List.of(challengeReadResponse), pageRequest, 1);
        given(challengeRepository.search(anyString(), anyString(), any())).willReturn(mockPage);

        // when
        Page<ChallengeReadResponse> response = challengeService.searchChallenge("제목", "내용", 1, 10);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getContent().get(0).title()).isEqualTo("제목");
    }

    @Test
    @DisplayName("챌린지를 상세하게 조회 할수 있다.")
    void searchChallengeById() {

        // given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("사용자")
                .build();
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        given(challengeRepository.findWithCreatorById(any())).willReturn(Optional.of(challenge));

        // when
        ChallengeReadDetailResponse response = challengeService.searchChallengeById(1L);

        // then
        assertThat(response.title()).isEqualTo("제목");
    }

    @Test
    @DisplayName("존재하지 않는 챌린지는 조회할수 없다.")
    void notSearchChallengeById() {

        // given
        given(challengeRepository.findWithCreatorById(any())).willReturn(Optional.empty());

        // when
        // then
        assertThatThrownBy(() -> challengeService.searchChallengeById(99L)).isInstanceOf(ChallengeException.class);
    }

    @Test
    @DisplayName("챌린지를 수정 할수 있다.")
    void updateChallenge() {

        // given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("사용자")
                .build();
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        given(challengeRepository.findByIdAndCreatorId(any(), any())).willReturn(Optional.of(challenge));

        LocalDate start = LocalDate.of(2024, 2, 1);
        LocalDate end = LocalDate.of(2024, 3, 1);
        ChallengeUpdateRequest updateRequest = new ChallengeUpdateRequest("수정", "수정", "PUBLIC", 1000, 5, "NONE", start, end);

        //when
        ChallengeUpdateResponse challengeUpdateResponse = challengeService.updateChallenge(creator, 1L, updateRequest);

        //then
        assertThat(challenge.getTitle()).isEqualTo("수정");
        assertThat(challengeUpdateResponse.title()).isEqualTo("수정");
    }

    @Test
    @DisplayName("만든 사람만 챌린지를 수정 할수 있다.")
    void updateNotChallenge() {

        //given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("작성자")
                .email("이메일1")
                .build();

        ReflectionTestUtils.setField(creator, "id", 1L);

        UserEntity noCreator = UserEntity.builder()
                .name("사용자")
                .email("이메일2")
                .build();

        ReflectionTestUtils.setField(noCreator, "id", 2L);


        Challenge challenge = Challenge.of(challengeCreateRequest, creator);


        given(challengeRepository.findByIdAndCreatorId(1L, creator.getId())).willReturn(Optional.of(challenge));
        given(challengeRepository.findByIdAndCreatorId(1L, noCreator.getId())).willReturn(Optional.empty());

        LocalDate start = LocalDate.of(2024, 2, 1);
        LocalDate end = LocalDate.of(2024, 3, 1);
        ChallengeUpdateRequest updateRequest = new ChallengeUpdateRequest("수정", "수정", "PUBLIC", 1000, 5, "NONE", start, end);

        //when
        challengeService.deleteChallenge(creator, 1L);
        //then
        assertThatThrownBy(() -> challengeService.updateChallenge(noCreator, 1L, updateRequest)).isInstanceOf(ChallengeException.class)
                .hasMessage("수정 및 삭제 권한이 없습니다.");
    }

    @Test
    @DisplayName("챌린지를 삭제 할수 있다.")
    void deleteNotChallenge() {
        //given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("작성자")
                .build();
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        given(challengeRepository.findByIdAndCreatorId(any(), any())).willReturn(Optional.of(challenge));

        //when
        challengeService.deleteChallenge(creator, 1L);

        //then
        verify(challengeRepository, times(1)).delete(challenge);
    }

    @Test
    @DisplayName("만든 사람만 챌린지를 삭제 할수 있다.")
    void deleteChallenge() {
        //given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("작성자")
                .email("이메일1")
                .build();

        ReflectionTestUtils.setField(creator, "id", 1L);

        UserEntity noCreator = UserEntity.builder()
                .name("사용자")
                .email("이메일2")
                .build();

        ReflectionTestUtils.setField(noCreator, "id", 2L);


        Challenge challenge = Challenge.of(challengeCreateRequest, creator);

        given(challengeRepository.findByIdAndCreatorId(1L, creator.getId())).willReturn(Optional.of(challenge));
        given(challengeRepository.findByIdAndCreatorId(1L, noCreator.getId())).willReturn(Optional.empty());

        //when
        challengeService.deleteChallenge(creator, 1L);
        //then
        assertThatThrownBy(() -> challengeService.deleteChallenge(noCreator, 1L)).isInstanceOf(ChallengeException.class)
                .hasMessage("수정 및 삭제 권한이 없습니다.");
    }


    @Test
    @DisplayName("사용자는 챌린지를 참여할수 있다.")
    void participate() {
        // given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity creator = UserEntity.builder()
                .name("작성자")
                .build();
        UserEntity noCreator = UserEntity.builder()
                .name("사용자")
                .build();
        Challenge challenge = Challenge.of(challengeCreateRequest, creator);
        given(challengeRepository.findById(any())).willReturn(Optional.of(challenge));
        given(userChallengeRepository.existsByUserIdAndChallengeId(any(), any())).willReturn(false);
        given(userChallengeRepository.countByChallengeId(1L)).willReturn(4L);
        // when
        challengeService.participate(noCreator, 1L);
        // then
        verify(userChallengeRepository, times(1)).save(any());
    }

    @Test
    @DisplayName("사용자는 참여한 챌린지를 참여할수 없다.")
    void NotParticipate() {
        //given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity user = UserEntity.builder()
                .name("사용자")
                .email("이메일1")
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);

        Challenge challenge = Challenge.of(challengeCreateRequest, user);
        given(challengeRepository.findById(any())).willReturn(Optional.of(challenge));
        given(userChallengeRepository.existsByUserIdAndChallengeId(any(), any())).willReturn(true);
        // when
        // then
        assertThatThrownBy(() -> challengeService.participate(user, 1L)).isInstanceOf(ChallengeException.class)
                .hasMessage("이미 참여된 챌린지 입니다.");

    }

    @Test
    @DisplayName("모집인원 초과한 챌린지는 참여할수 없다.")
    void FullParticipate() {
        // given
        ChallengeCreateRequest challengeCreateRequest = createRequest();
        UserEntity user = UserEntity.builder()
                .name("사용자")
                .email("이메일1")
                .build();

        ReflectionTestUtils.setField(user, "id", 1L);
        Challenge challenge = Challenge.of(challengeCreateRequest, user);
        given(challengeRepository.findById(any())).willReturn(Optional.of(challenge));
        given(userChallengeRepository.existsByUserIdAndChallengeId(any(), any())).willReturn(false);
        given(userChallengeRepository.countByChallengeId(any())).willReturn(5L);
        // when
        // then
        assertThatThrownBy(() -> challengeService.participate(user, 1L)).isInstanceOf(ChallengeException.class)
                .hasMessage("참여 인원이 초과 하였습니다.");

    }
}