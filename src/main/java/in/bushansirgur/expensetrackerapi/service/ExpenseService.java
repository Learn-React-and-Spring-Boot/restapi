package in.bushansirgur.expensetrackerapi.service;

import java.sql.Date;
import java.util.List;

import in.bushansirgur.expensetrackerapi.dto.ExpenseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import in.bushansirgur.expensetrackerapi.entity.Expense;

public interface ExpenseService {
	
	List<ExpenseDTO> getAllExpenses(Pageable page);
	
	ExpenseDTO getExpenseById(String id);
	
	void deleteExpenseById(String expenseId);

	ExpenseDTO saveExpenseDetails(ExpenseDTO expenseDTO);

	ExpenseDTO updateExpenseDetails(String expenseId, ExpenseDTO expenseDTO);
	
	List<Expense> readByCategory(String category, Pageable page);
	
	List<Expense> readByName(String keyword, Pageable page); 
	
	List<Expense> readByDate(Date startDate, Date endDate, Pageable page);
}
