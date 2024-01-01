package hu.webuni.student.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.envers.Audited;

@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
@Entity
@Audited
@Builder
public class Image {
    @Id
    @GeneratedValue
    private long id;
    private String fileName;
    //@Lob
    //@Type(type="org.hibernate.type.BinaryType") // A Hibernate 6 esetében már nem a @Type annotációval lehet elérni, hogy a bináris adatokat bytea típusra képezze le Postgres-ben, hanem egyszerűen el kell hagyni a @Lob annotációt:
    private byte[] data;
}
