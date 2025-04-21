package dev.book.accountbook.service;

import dev.book.accountbook.dto.event.SpendCreatedEvent;
import dev.book.accountbook.dto.request.AccountBookIncomeRequest;
import dev.book.accountbook.dto.request.AccountBookRequest;
import dev.book.accountbook.dto.request.AccountBookSpendRequest;
import dev.book.accountbook.dto.response.AccountBookIncomeResponse;
import dev.book.accountbook.dto.response.AccountBookSpendResponse;
import dev.book.accountbook.entity.AccountBook;
import dev.book.accountbook.exception.accountbook.AccountBookErrorCode;
import dev.book.accountbook.exception.accountbook.AccountBookErrorException;
import dev.book.accountbook.repository.AccountBookRepository;
import dev.book.accountbook.type.CategoryType;
import dev.book.challenge.rank.SpendCreatedRankingEvent;
import dev.book.global.entity.Category;
import dev.book.global.repository.CategoryRepository;
import dev.book.user.entity.UserEntity;
import dev.book.user.exception.UserErrorCode;
import dev.book.user.exception.UserErrorException;
import dev.book.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountBookService {
    private final UserRepository userRepository;
    private final ApplicationEventPublisher publisher;
    private final CategoryRepository categoryRepository;
    private final AccountBookRepository accountBookRepository;

    @Transactional
    public AccountBookSpendResponse getSpendOne(Long id, Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new UserErrorException(UserErrorCode.USER_NOT_FOUND));
        AccountBook accountBook = findAccountBookOrThrow(id, userId, AccountBookErrorCode.NOT_FOUND_SPEND);

        return AccountBookSpendResponse.from(accountBook);
    }

    @Transactional
    public List<AccountBookSpendResponse> getSpendList(Long userId) {
        List<AccountBook> accountBooks = accountBookRepository.findAllByUserIdAndTypeOrderByUpdatedAtDesc(userId, CategoryType.SPEND);

        return accountBooks.stream()
                .map(AccountBookSpendResponse::from)
                .toList();
    }

    @Transactional
    public AccountBookSpendResponse createSpend(AccountBookSpendRequest request, UserEntity user) {
        List<Category> categoryList = findCategorySpendList(request);
        AccountBook accountBook = request.toEntity(user);
        categoryList.forEach(accountBook::addCategory);
        AccountBook saved = accountBookRepository.save(accountBook);

        publisher.publishEvent(new SpendCreatedEvent(user.getId(), user.getNickname()));
        publisher.publishEvent(new SpendCreatedRankingEvent(accountBook));

        return AccountBookSpendResponse.from(saved);
    }

    @Transactional
    public AccountBookSpendResponse modifySpend(AccountBookSpendRequest request, Long id, Long userId) {
        AccountBook accountBook = findAccountBookOrThrow(id, userId, AccountBookErrorCode.NOT_FOUND_SPEND);
        updateAccountBook(accountBook, request);

        List<Category> categoryList = findCategorySpendList(request);
        accountBook.modifyCategories(categoryList);

        accountBookRepository.flush();

        return AccountBookSpendResponse.from(accountBook);
    }

    @Transactional
    public boolean deleteSpend(Long id, Long userId) {
        AccountBook accountBook = findAccountBookOrThrow(id, userId, AccountBookErrorCode.NOT_FOUND_SPEND);
        accountBookRepository.delete(accountBook);

        return true;
    }

    @Transactional
    public AccountBookIncomeResponse getIncomeOne(Long id, Long userId) {
        AccountBook accountBook = findAccountBookOrThrow(id, userId, AccountBookErrorCode.NOT_FOUND_INCOME);

        return AccountBookIncomeResponse.from(accountBook);
    }

    @Transactional
    public List<AccountBookIncomeResponse> getIncomeList(Long userId) {
        List<AccountBook> accountBooks = accountBookRepository.findAllByUserIdAndTypeOrderByUpdatedAtDesc(userId, CategoryType.INCOME);

        return accountBooks.stream()
                .map(AccountBookIncomeResponse::from)
                .toList();
    }

    @Transactional
    public AccountBookIncomeResponse createIncome(AccountBookIncomeRequest request, UserEntity user) {
        List<Category> categoryList = findCategorySpendList(request);
        AccountBook accountBook = request.toEntity(user);
        categoryList.forEach(accountBook::addCategory);
        AccountBook saved = accountBookRepository.save(accountBook);

        return AccountBookIncomeResponse.from(saved);
    }

    @Transactional
    public AccountBookIncomeResponse modifyIncome(Long id, AccountBookIncomeRequest request, Long userId) {
        AccountBook accountBook = findAccountBookOrThrow(id, userId, AccountBookErrorCode.NOT_FOUND_INCOME);
        updateAccountBook(accountBook, request);

        List<Category> categoryList = findCategorySpendList(request);
        accountBook.modifyCategories(categoryList);

        return AccountBookIncomeResponse.from(accountBook);
    }

    @Transactional
    public boolean deleteIncome(Long id, Long userId) {
        AccountBook accountBook = findAccountBookOrThrow(id, userId, AccountBookErrorCode.NOT_FOUND_INCOME);
        accountBookRepository.delete(accountBook);

        return true;
    }

    public List<AccountBookSpendResponse> getCategorySpendList(String category, Long userId) {
        Page<AccountBook> categorySpendList = accountBookRepository.findByUserIdAndCategoryNameWithGraph(userId, category, PageRequest.of(0, 10));

        return categorySpendList.stream()
                .map(AccountBookSpendResponse::from)
                .toList();
    }

    private AccountBook findAccountBookOrThrow(Long id, Long userId, AccountBookErrorCode errorCode) {
        return accountBookRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new AccountBookErrorException(errorCode));
    }

    private void updateAccountBook(AccountBook accountBook, AccountBookRequest request) {
        accountBook.modifyTitle(request.title());
        accountBook.modifyAmount(request.amount());
        accountBook.modifyMemo(request.memo());
        accountBook.modifyFrequency(request.repeat().frequency());
        accountBook.modifyMonth(request.repeat().month());
        accountBook.modifyDay(request.repeat().day());
        accountBook.modifyEndDate(request.endDate());

        List<Category> categoryList = findCategorySpendList(request);
        accountBook.modifyCategories(categoryList);
    }

    private List<Category> findCategorySpendList(AccountBookRequest request) {
        List<Category> categoryList = categoryRepository.findByCategoryIn(request.categoryList());

        if (categoryList.size() != request.categoryList().size()) {

            throw new AccountBookErrorException(AccountBookErrorCode.NOT_FOUND_CATEGORY);
        }

        return categoryList;
    }
}
