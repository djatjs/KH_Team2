package cafePro_DB.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cafePro_DB.model.vo.Category;

public interface CategoryDAO {
	
	boolean checkCategory(Category category);
	
	List<Category> seletAllCategory();

	void insertCategory(@Param("category") Category category);

	Category seletCategory(@Param("can") int can);

	boolean updateCategory(@Param("cam") String cam, @Param("cac") String cac, @Param("can") int can);




	

	
}