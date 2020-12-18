package ru.classificator.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.classificator.entities.TextEntity;

@Repository

public interface TextRepository extends JpaRepository <TextEntity,Integer> {

}