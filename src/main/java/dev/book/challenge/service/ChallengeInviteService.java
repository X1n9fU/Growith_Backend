package dev.book.challenge.service;

import dev.book.challenge.challenge_invite.entity.ChallengeInvite;
import dev.book.challenge.challenge_invite.repository.ChallengeInviteRepository;
import dev.book.challenge.dto.request.ChallengeInviteRequest;
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
            throw new ChallengeException(CHALLENGE_INVITE_INVALID);
        }
        boolean isAlreadyInvited = challengeInviteRepository.existsByInviteUserIdAndChallengeId(inviteUser.getId(), challenge.getId());
        if (isAlreadyInvited) {
            throw new ChallengeException(CHALLENGE_ALREADY_INVITED);
        }
        ChallengeInvite challengeInvite = ChallengeInvite.of(user, inviteUser, challenge);
        challengeInviteRepository.save(challengeInvite);
    }

    private boolean isNotParticipant(Long challengeId, UserEntity user) {
        return !userChallengeRepository.existsByUserIdAndChallengeId(user.getId(), challengeId);
    }

}
