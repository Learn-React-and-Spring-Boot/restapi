package in.bushansirgur.expensetrackerapi.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import in.bushansirgur.expensetrackerapi.dto.ExpenseDTO;
import in.bushansirgur.expensetrackerapi.entity.Category;
import in.bushansirgur.expensetrackerapi.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import in.bushansirgur.expensetrackerapi.entity.Expense;
import in.bushansirgur.expensetrackerapi.exceptions.ResourceNotFoundException;
import in.bushansirgur.expensetrackerapi.repository.ExpenseRepository;

@Service
public class ExpenseServiceImpl implements ExpenseService {

	@Autowired
	private ExpenseRepository expenseRepo;
	
	@Autowired
	private UserService userService;

	@Autowired
	private CategoryRepository categoryRepository;
	
	@Override
	public List<ExpenseDTO> getAllExpenses(Pageable page) {
		List<Expense> listOfExpenses = expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page).toList();
		return listOfExpenses.stream().map(expense -> mapToDTO(expense)).collect(Collectors.toList());
	}

	@Override
	public Expense getExpenseById(String expenseId){
		Optional<Expense> expense = expenseRepo.findByUserIdAndExpenseId(userService.getLoggedInUser().getId(), expenseId);
		if (expense.isPresent()) {
			return expense.get();
		}
		throw new ResourceNotFoundException("Expense is not found for the id "+expenseId);
	}

	@Override
	public void deleteExpenseById(String expenseId) {
		Expense expense = getExpenseById(expenseId);
		expenseRepo.delete(expense);
	}

	@Override
	public ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO) {
		Optional<Category> optionalCategory = categoryRepository.findByUserIdAndCategoryId(userService.getLoggedInUser().getId(), expenseDTO.getCategoryId());
		if (!optionalCategory.isPresent()) {
			throw new ResourceNotFoundException("Category not found for the categoryId "+expenseDTO.getCategoryId());
		}
		expenseDTO.setCategory(optionalCategory.get());
		expenseDTO.setExpenseId(UUID.randomUUID().toString());
		expenseDTO.setUser(userService.getLoggedInUser());
		Expense newExpense = mapToEntity(expenseDTO);
		newExpense = expenseRepo.save(newExpense);
		return mapToDTO(newExpense);
	}

	@Override
	public Expense updateExpenseDetails(String expenseId, Expense expense){
		Expense existingExpense = getExpenseById(expenseId);
		existingExpense.setName(expense.getName() != null ? expense.getName() : existingExpense.getName());
		existingExpense.setDescription(expense.getDescription() != null ? expense.getDescription() : existingExpense.getDescription());
		existingExpense.setCategory(expense.getCategory() != null ? expense.getCategory() : existingExpense.getCategory());
		existingExpense.setDate(expense.getDate() != null ? expense.getDate() : existingExpense.getDate());
		existingExpense.setAmount(expense.getAmount() != null ? expense.getAmount() : existingExpense.getAmount());
		return expenseRepo.save(existingExpense);
	}

	@Override
	public List<Expense> readByCategory(String category, Pageable page) {
		return expenseRepo.findByUserIdAndCategory(userService.getLoggedInUser().getId(), category, page).toList();
	}

	@Override
	public List<Expense> readByName(String keyword, Pageable page) {
		return expenseRepo.findByUserIdAndNameContaining(userService.getLoggedInUser().getId(), keyword, page).toList();
	}

	@Override
	public List<Expense> readByDate(Date startDate, Date endDate, Pageable page) {
		
		if (startDate == null) {
			startDate = new Date(0);
		}
		
		if (endDate == null) {
			endDate = new Date(System.currentTimeMillis());
		}
		
		return expenseRepo.findByUserIdAndDateBetween(userService.getLoggedInUser().getId(), startDate, endDate, page).toList();
	}

	private Expense mapToEntity(ExpenseDTO expenseDTO) {
		return Expense.builder()
				.expenseId(expenseDTO.getExpenseId())
				.name(expenseDTO.getName())
				.description(expenseDTO.getDescription())
				.amount(expenseDTO.getAmount())
				.date(expenseDTO.getDate())
				.category(expenseDTO.getCategory())
				.user(expenseDTO.getUser())
				.build();
	}

	private ExpenseDTO mapToDTO(Expense newExpense) {
		return ExpenseDTO.builder()
				.expenseId(newExpense.getExpenseId())
				.name(newExpense.getName())
				.description(newExpense.getDescription())
				.amount(newExpense.getAmount())
				.date(newExpense.getDate())
				.category(newExpense.getCategory())
				.user(newExpense.getUser())
				.createdAt(newExpense.getCreatedAt())
				.updatedAt(newExpense.getUpdatedAt())
				.build();
	}

}



























