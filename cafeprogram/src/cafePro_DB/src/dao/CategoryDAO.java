package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.vo.Category;

public interface CategoryDAO {
	
	Category selectCategoryByName(@Param("category")Category category);

	boolean insertCategory(@Param("category")Category category);

	List<Category> seletAllCategory();

	Category seletCategoryByNum(int caNum);

	boolean updateCategory(
			@Param("caNum")int caNum, 
			@Param("caName")String caName, 
			@Param("caCode")String caCode);

	boolean deleteCategory(int caNum);

	Category seletCategory(
			@Param("caName")String caName, 
			@Param("caCode")String caCode);

	boolean categoryExists(String caName);

	




	

	
}