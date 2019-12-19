package de.mathit.repository;

import java.util.List;

public interface Repository<T> {

  List<T> query(Query query);

}
