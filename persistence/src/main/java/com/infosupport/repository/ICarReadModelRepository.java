package com.infosupport.repository;

import com.infosupport.entity.CarEntity;
import org.springframework.data.repository.ListCrudRepository;

public interface ICarReadModelRepository extends ListCrudRepository<CarEntity, String> {
}
