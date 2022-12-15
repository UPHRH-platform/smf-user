package com.tarento.retail.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.tarento.retail.model.InstituteCourses;


@Repository
public interface InstituteCoursesDao extends CrudRepository<InstituteCourses, Long>{

	List<InstituteCourses> findByProfileId(Long userId);

}
