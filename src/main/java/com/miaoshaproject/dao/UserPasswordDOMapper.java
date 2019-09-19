package com.miaoshaproject.dao;

import com.miaoshaproject.dataobject.UserPasswordDO;
import org.springframework.stereotype.Repository;

@Repository
public interface UserPasswordDOMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int deleteByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int insert(UserPasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int insertSelective(UserPasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    UserPasswordDO selectByPrimaryKey(Long id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int updateByPrimaryKeySelective(UserPasswordDO record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    int updateByPrimaryKey(UserPasswordDO record);
    /**
     * Author:wpfei
     * This method corresponds to the database table user_password
     *
     * @mbg.generated Sat Jun 29 16:33:41 CST 2019
     */
    UserPasswordDO selectByUserId(Long userId);
}