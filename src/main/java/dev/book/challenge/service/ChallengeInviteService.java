package dev.book.challenge.service;

import dev.book.challenge.challenge_invite.entity.ChallengeInvite;
import dev.book.challenge.challenge_invite.repository.ChallengeInviteRepository;
import dev.book.challenge.dto.request.ChallengeInviteRequest;
import dev.book.challenge.dto.response.ChallengeInviteResponse;
import dev.book.challenge.entity.Challenge;
import dev.book.challenge.exception.ChallengeException;
import dev.book.challenge.repository.ChallengeRepository;
import dev.book.challenge.user_challenge.repository.UserChallengeRepository;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static dev.book.challenge.exception.ErrorCode.*;
import static dev.book.user.exception.UserErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChallengeInviteService {

    private final ChallengeInviteRepository challengeInviteRepository;
    private final ChallengeRepository challengeRepository;
    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void invite(Long challengeId, UserEntity user, ChallengeInviteRequest challengeInviteRequest) {
        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND));
        UserEntity inviteUser = userRepository.findByEmail(challengeInviteRequest.email()).orElseThrow(() -> new UserErrorException(USER_NOT_FOUND));

        boolean isNotParticipant = isNotParticipant(challengeId, user);
        if (isNotParticipant) {
            throw new ChallengeException(CHALLENGE_INVITE_INVALID); // 현재 내가 챌린지에 참여하지 않는데 초대를 보낼때
        }
        boolean isAlreadyInvited = isAlreadyInvited(inviteUser, challenge);

        if (isAlreadyInvited) {
            throw new ChallengeException(CHALLENGE_ALREADY_INVITED); // 이미 초대가 된 상황일때
        }
        long countParticipants = userChallengeRepository.countByChallengeId(challengeId);
        if (challenge.isOver(countParticipants)) {
            throw new ChallengeException(CHALLENGE_CAPACITY_FULL); // 최대인원을 초과 했을때
        }
        ChallengeInvite challengeInvite = ChallengeInvite.of(user, inviteUser, challenge);
        challengeInviteRepository.save(challengeInvite);
    }

    private boolean isAlreadyInvited(UserEntity inviteUser, Challenge challenge) {
        return challengeInviteRepository.existsByInviteUserIdAndChallengeId(inviteUser.getId(), challenge.getId());
    }

    private boolean isNotParticipant(Long challengeId, UserEntity user) {
        return !userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId);
    }

    @Transactional(readOnly = true)
    public List<ChallengeInviteResponse> getMyInviteList(UserEntity user) {
        List<ChallengeInvite> challengeInvites = challengeInviteRepository.findAllByInviteUserId(user.getId());
        List<ChallengeInviteResponse> challengeInviteResponses = challengeInvites.stream().map(ChallengeInviteResponse::fromEntity).toList();
        return challengeInviteResponses;
    }

    @Transactional
    public void acceptInvite(Long inviteId, UserEntity user) {
        ChallengeInvite challengeInvite = challengeInviteRepository.findByIdAndInviteUserId(inviteId, user.getId()).orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND_INVITED));
        challengeInvite.accept();
    }

    @Transactional
    public void rejectInvite(Long inviteId, UserEntity user) {
        ChallengeInvite challengeInvite = challengeInviteRepository.findByIdAndInviteUserId(inviteId, user.getId()).orElseThrow(() -> new ChallengeException(CHALLENGE_NOT_FOUND_INVITED));
        challengeInvite.reject();
    }
}
