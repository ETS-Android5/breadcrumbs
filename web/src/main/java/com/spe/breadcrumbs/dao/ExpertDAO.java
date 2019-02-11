package com.spe.breadcrumbs.dao;

import com.spe.breadcrumbs.entity.Expert;

import java.util.List;

public interface ExpertDAO {
    public Expert getExpert(Long id);
    List<Expert> getAllExperts();
    public boolean validate(String email,String password);
}
