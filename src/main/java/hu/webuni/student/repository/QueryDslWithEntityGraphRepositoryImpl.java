package hu.webuni.student.repository;

import com.querydsl.core.types.EntityPath;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import hu.webuni.student.model.Course;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.querydsl.SimpleEntityPathResolver;

import jakarta.persistence.EntityManager;
import java.util.List;

public class QueryDslWithEntityGraphRepositoryImpl extends SimpleJpaRepository<Course, Long> implements QueryDslWithEntityGraphRepository<Course, Long> {

    private EntityManager em;
    private EntityPath<Course> path;
    private PathBuilder<Course> builder;
    private Querydsl querydsl;

    public QueryDslWithEntityGraphRepositoryImpl(EntityManager em) {
        super(Course.class, em);
        this.em = em;
        this.path = SimpleEntityPathResolver.INSTANCE.createPath(Course.class);
        this.builder = new PathBuilder<>(path.getType(), path.getMetadata());
        this.querydsl = new Querydsl(em, builder);

    }

    @Override
    public List<Course> findAll(Predicate predicate, String entityGraphName, EntityGraph.EntityGraphType egType, Sort sort) {

        //JPAQuery query = querydsl.createQuery(path).where(predicate);
        // sort hozzaadasa:
        JPAQuery query = (JPAQuery) querydsl.applySorting(sort, querydsl.createQuery(path).where(predicate));

        query.setHint(egType.getKey(), em.getEntityGraph(entityGraphName));
        return query.fetch();
    }
}
