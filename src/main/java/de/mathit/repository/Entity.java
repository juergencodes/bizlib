package de.mathit.repository;

public interface Entity {

  String getName();

  <T> T get(String name);

  <T> void set(String name, T value);

}