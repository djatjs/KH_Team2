package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.vo.Category;
import model.vo.Menu;
import model.vo.Tag;


public interface MenuDAO {
	
	Menu selectMenuByName(@Param("menu")Menu menu);

	boolean insertMenu(@Param("menu")Menu menu);

	List<Menu> seletAllMenu();

	Menu seletMenuByCode(String meCode);

	boolean updateMenu(
			@Param("meCode")int meCode,
			@Param("mecaCode")int mecaCode,
			@Param("meName")String meName,
			@Param("mePrice")int mePrice,
			@Param("meContent")String meContent,
			@Param("meThumb")String meThumb,
			@Param("meAble")String meAble,
			@Param("meHotIce")String meHotIce);
			
	boolean deleteMenu(int meCode);

	Menu seletMenu(
			@Param("meName")String meName, 
			@Param("mePrice")int mePrice);

	boolean menuExists(String meName);

	boolean updateMenu(@Param("meCode")String meCode, @Param("meName")String meName, @Param("mePrice")int mePrice);

	boolean deleteCategory(String meCode);

	

}
