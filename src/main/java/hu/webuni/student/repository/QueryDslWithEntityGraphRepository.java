package hu.webuni.student.repository;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

public interface QueryDslWithEntityGraphRepository<T, ID> {
    List<T> findAll(Predicate predicate, String entityGraphName, EntityGraph.EntityGraphType egType, Sort sort);
}
