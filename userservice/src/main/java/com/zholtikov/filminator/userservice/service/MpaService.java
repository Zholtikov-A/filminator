package com.zholtikov.filminator.userservice.service;

import com.zholtikov.filminator.userservice.dao.MpaDao;
import com.zholtikov.filminator.userservice.dao.MpaDao;
import com.zholtikov.filminator.userservice.model.Mpa;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MpaService {
    private final MpaDao mpaDao;

    public List<Mpa> findAll() {
        return mpaDao.findAll();
    }

    public Mpa findMpaById(Long id) {
        mpaDao.checkMpaExistence(id);
        return mpaDao.findMpaById(id);
    }

}
