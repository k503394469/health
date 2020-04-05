package com.liu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.liu.dao.MemberDao;
import com.liu.pojo.Member;
import com.liu.service.MemberService;
import com.liu.utils.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    private MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        String password = member.getPassword();
        if (password!=null){
            //用户设置了密码,进行MD5
            password=MD5Utils.md5(password);
            member.setPassword(password);
        }
        memberDao.add(member);
    }
}
