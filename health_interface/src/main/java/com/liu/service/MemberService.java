package com.liu.service;


import com.liu.pojo.Member;

public interface MemberService {
    Member findByTelephone(String telephone);

    void add(Member member);
}
