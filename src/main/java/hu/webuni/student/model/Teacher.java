    package hu.webuni.student.model;

    import lombok.*;
    import lombok.experimental.SuperBuilder;
    import org.hibernate.envers.Audited;

    import jakarta.persistence.*;
    import java.time.LocalDate;
    import java.util.Set;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @SuperBuilder
    @EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
    @Entity
    @Cacheable
    @Audited
    @Data
    @ToString(onlyExplicitlyIncluded = true)
    public class Teacher extends AppUser {

        /*
        @Id
        @GeneratedValue//(strategy = GenerationType.AUTO)
        @EqualsAndHashCode.Include()
        private long id;

         */

        @ManyToMany(mappedBy = "teachers")
        private Set<Course> courses;

        /*
        //@Size(min = 3, max = 20)
        private String name;
        //@Size (min = 3, max = 10)
        private LocalDate birthdate;

        public Teacher(String name, LocalDate birthdate) {
            this.name = name;
            this.birthdate = birthdate;
        }



         */



        @Override
        public UserType getUserType() {
            return UserType.TEACHER;
        }
    }
