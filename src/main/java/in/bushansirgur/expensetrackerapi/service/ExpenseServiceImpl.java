package in.bushansirgur.expensetrackerapi.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
	
	@Override
	public Page<Expense> getAllExpenses(Pageable page) {
		return expenseRepo.findByUserId(userService.getLoggedInUser().getId(), page);
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
	public Expense saveExpenseDetails(Expense expense) {
		expense.setExpenseId(UUID.randomUUID().toString());
		expense.setUser(userService.getLoggedInUser());
		return expenseRepo.save(expense);
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

}



























