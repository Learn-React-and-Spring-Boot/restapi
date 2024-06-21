package in.bushansirgur.expensetrackerapi.controller;

import java.sql.Date;
import java.util.List;
import java.util.stream.Collectors;

import in.bushansirgur.expensetrackerapi.dto.ExpenseDTO;
import in.bushansirgur.expensetrackerapi.entity.Category;
import in.bushansirgur.expensetrackerapi.io.CategoryResponse;
import in.bushansirgur.expensetrackerapi.io.ExpenseRequest;
import in.bushansirgur.expensetrackerapi.io.ExpenseResponse;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import in.bushansirgur.expensetrackerapi.entity.Expense;
import in.bushansirgur.expensetrackerapi.service.ExpenseService;
import jakarta.validation.Valid;

@CrossOrigin("*")
@RestController
public class ExpenseController {

	@Autowired
	private ExpenseService expenseService;
	
	@GetMapping("/expenses")
	public List<ExpenseResponse> getAllExpenses(Pageable page) {
		List<ExpenseDTO> listOfExpensesDTO = expenseService.getAllExpenses(page);
		return listOfExpensesDTO.stream().map(expenseDTO -> mapToResponse(expenseDTO)).collect(Collectors.toList());
	}
	
	@GetMapping("/expenses/{id}")
	public ExpenseResponse getExpenseById(@PathVariable String id){
		ExpenseDTO expenseDTO = expenseService.getExpenseById(id);
		return mapToResponse(expenseDTO);
	}
	
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	@DeleteMapping("/expenses/{id}")
	public void deleteExpenseById(@PathVariable String id) {
		expenseService.deleteExpenseById(id);
	}
	
	@ResponseStatus(value = HttpStatus.CREATED)
	@PostMapping("/expenses")
	public ExpenseResponse saveExpenseDetails(@Valid @RequestBody ExpenseRequest expenseRequest) {
		ExpenseDTO expenseDTO = mapToDTO(expenseRequest);
		expenseDTO = expenseService.saveExpenseDetails(expenseDTO);
		return mapToResponse(expenseDTO);
	}

	@PutMapping("/expenses/{id}")
	public ExpenseResponse updateExpenseDetails(@RequestBody ExpenseRequest expenseRequest, @PathVariable String id){
		ExpenseDTO expenseDTO = mapToDTO(expenseRequest);
		expenseDTO = expenseService.updateExpenseDetails(id, expenseDTO);
		return mapToResponse(expenseDTO);
	}
	
	@GetMapping("/expenses/category")
	public List<Expense> getExpensesByCategory(@RequestParam String category, Pageable page) {
		return expenseService.readByCategory(category, page);
	}
	
	@GetMapping("/expenses/name")
	public List<Expense> getExpensesByName(@RequestParam String keyword, Pageable page) {
		return expenseService.readByName(keyword, page);
	}
	
	@GetMapping("/expenses/date")
	public List<Expense> getExpensesByDates(@RequestParam(required = false) Date startDate,
											@RequestParam(required = false) Date endDate,
											Pageable page) {
		return expenseService.readByDate(startDate, endDate, page);
	}

	private ExpenseDTO mapToDTO(ExpenseRequest expenseRequest) {
		return ExpenseDTO.builder()
				.name(expenseRequest.getName())
				.description(expenseRequest.getDescription())
				.amount(expenseRequest.getAmount())
				.date(expenseRequest.getDate())
				.categoryId(expenseRequest.getCategoryId())
				.build();
	}

	private ExpenseResponse mapToResponse(ExpenseDTO expenseDTO) {
		return ExpenseResponse.builder()
				.expenseId(expenseDTO.getExpenseId())
				.name(expenseDTO.getName())
				.description(expenseDTO.getDescription())
				.amount(expenseDTO.getAmount())
				.date(expenseDTO.getDate())
				.categoryResponse(mapToCategoryResponse(expenseDTO.getCategory()))
				.createdAt(expenseDTO.getCreatedAt())
				.updatedAt(expenseDTO.getUpdatedAt())
				.build();
	}

	private CategoryResponse mapToCategoryResponse(Category category) {
		CategoryResponse returnValue = new CategoryResponse();
		BeanUtils.copyProperties(category, returnValue);
		return returnValue;
	}
}






















