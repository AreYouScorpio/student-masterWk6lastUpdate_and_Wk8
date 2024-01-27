package hu.webuni.student.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.envers.Audited;

import java.time.LocalDate;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Audited
public class SpecialDay {

    @Id
    @GeneratedValue
    @EqualsAndHashCode.Include
    private int id;
    private LocalDate sourceDay;
    private LocalDate targetDay;	//null means sourceDay is holiday

}
