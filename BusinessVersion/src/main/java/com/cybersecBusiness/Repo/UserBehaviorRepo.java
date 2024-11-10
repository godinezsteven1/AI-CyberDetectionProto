package com.cybersecBusiness.Repo;

import com.cybersecBusiness.Model.UserBehaviorModel;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.FluentQuery;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * The {@code UserBehaviorRepo} class is meant to keep the data of user patterns. The goal is
 * to implement a Machine Learning tool(smile) to enhance behavioral analysis.
 */
@Repository
public class UserBehaviorRepo implements MongoRepository<UserBehaviorModel, String> {

  @Override
  public <S extends UserBehaviorModel> S save(S entity) {
    return null;
  }

  @Override
  public <S extends UserBehaviorModel> List<S> saveAll(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public Optional<UserBehaviorModel> findById(String s) {
    return Optional.empty();
  }

  @Override
  public boolean existsById(String s) {
    return false;
  }

  @Override
  public List<UserBehaviorModel> findAll() {
    return List.of();
  }

  @Override
  public Iterable<UserBehaviorModel> findAllById(Iterable<String> strings) {
    return null;
  }

  @Override
  public long count() {
    return 0;
  }

  @Override
  public void deleteById(String s) {

  }

  @Override
  public void delete(UserBehaviorModel entity) {

  }

  @Override
  public void deleteAllById(Iterable<? extends String> strings) {

  }

  @Override
  public void deleteAll(Iterable<? extends UserBehaviorModel> entities) {

  }

  @Override
  public void deleteAll() {

  }

  @Override
  public List<UserBehaviorModel> findAll(Sort sort) {
    return List.of();
  }

  @Override
  public Page<UserBehaviorModel> findAll(Pageable pageable) {
    return null;
  }

  @Override
  public <S extends UserBehaviorModel> S insert(S entity) {
    return null;
  }

  @Override
  public <S extends UserBehaviorModel> List<S> insert(Iterable<S> entities) {
    return List.of();
  }

  @Override
  public <S extends UserBehaviorModel> Optional<S> findOne(Example<S> example) {
    return Optional.empty();
  }

  @Override
  public <S extends UserBehaviorModel> List<S> findAll(Example<S> example) {
    return List.of();
  }

  @Override
  public <S extends UserBehaviorModel> List<S> findAll(Example<S> example, Sort sort) {
    return List.of();
  }

  @Override
  public <S extends UserBehaviorModel> Page<S> findAll(Example<S> example, Pageable pageable) {
    return null;
  }

  @Override
  public <S extends UserBehaviorModel> long count(Example<S> example) {
    return 0;
  }

  @Override
  public <S extends UserBehaviorModel> boolean exists(Example<S> example) {
    return false;
  }

  @Override
  public <S extends UserBehaviorModel, R> R findBy(Example<S> example,
                                                   Function<FluentQuery.FetchableFluentQuery<S>,
                                                           R> queryFunction) {
    return null;
  }
}
