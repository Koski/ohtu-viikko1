
package olutopas.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

@Entity
public class Rating {
    
    @Id
    int Id;
    
    @ManyToOne
    User user;
    
    @ManyToOne
    Beer beer;
    
    int rating;

    public Rating() {
    }

    public Rating(User user, Beer beer, int rating) {
        this.user = user;
        this.beer = beer;
        this.rating = rating;
    }

    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Beer getBeer() {
        return beer;
    }

    public void setBeer(Beer beer) {
        this.beer = beer;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    
    
}
