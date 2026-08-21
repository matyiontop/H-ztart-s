package hu.haztartas.repository;

import hu.haztartas.entity.Category;
import hu.haztartas.entity.enums.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    List<Category> findByTypeOrderByNameAsc(CategoryType type);
    List<Category> findByIsFixedOrderByNameAsc(boolean isFixed);
    Optional<Category> findByName(String name);
}
