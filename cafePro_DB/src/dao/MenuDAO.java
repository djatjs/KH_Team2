package dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import model.vo.Category;
import model.vo.Menu;
import model.vo.Order;
import model.vo.Tag;


public interface MenuDAO {
	
	Menu selectMenuByNameAndHI(@Param("menu")Menu menu);

	boolean insertMenu(@Param("menu")Menu menu);

	List<Menu> selectAllMenu();

	Menu selectMenuByCode(String meCode);

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

	Menu selectMenu(
			@Param("meName")String meName, 
			@Param("mePrice")int mePrice);

	boolean menuExists(@Param("meName")String meName, @Param("meHotIce")String meHotIce);

	boolean updateMenu(
			@Param("meCode")String meCode, 
			@Param("meName")String meName, 
			@Param("mePrice")int mePrice,
			@Param("meContent")String meContent, 
			@Param("meHotIce")String meHotIce);

	boolean deleteMenu(String meCode);

	List<Menu> selectMenu();
	

}
