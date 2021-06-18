package cn.edu.njust.mapper;

import cn.edu.njust.entity.Bank;

public interface AccountMapper {
//修改指定bank中的金额，金额直接赋值在bank的amount中
    public void modify(Bank bank);
//    通过accountID查找一条bank信息
    public Bank selectByID(String accountID);
}
