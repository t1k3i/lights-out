package models;

import jakarta.persistence.*;

@Entity
public class Problem {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, length = 64)
    private String description;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Player creator;

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public Player getCreator() {
        return creator;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreator(Player creator) {
        this.creator = creator;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", creator=" + creator +
                '}';
    }
}
