package dao;

import model.vo.Income;

public interface IncomeDAO {

	int incomeDay();

	int incomeMonth();

	int incomeYear();

	int totalIncome();

	boolean insertIncome(int resMoney);

}
