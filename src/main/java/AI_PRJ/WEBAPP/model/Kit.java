package AI_PRJ.WEBAPP.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "kits")
public class Kit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private Integer price;
    
    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "category_id")
    private Long categoryId;

    @Column(name = "image_url")
    private String imageUrl;

    @Column(name = "difficulty_level")
    private String difficultyLevel;

    @Column(name = "age_range")
    private String ageRange;

    @Column(name = "created_by")
    private Long createdBy;

    // One Kit has many Labs (mapped via intermediate table)
    @OneToMany(mappedBy = "kit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Lab> labs;

    public Kit() {
    }

    public Kit(Long id, String name, String description, Integer price, Integer stockQuantity, Long categoryId, String imageUrl,
           String difficultyLevel, String ageRange, Long createdBy, List<Lab> labs) {
    this.id = id;
    this.name = name;
    this.description = description;
    this.price = price;
    this.stockQuantity = stockQuantity;
    this.categoryId = categoryId;
    this.imageUrl = imageUrl;
    this.difficultyLevel = difficultyLevel;
    this.ageRange = ageRange;
    this.createdBy = createdBy;
    this.labs = labs;
}
   public Long getId() {
    return id;
}

public void setId(Long id) {
    this.id = id;
}

public String getName() {
    return name;
}

public void setName(String name) {
    this.name = name;
}

public String getDescription() {
    return description;
}

public void setDescription(String description) {
    this.description = description;
}

public Integer getPrice() {
    return price;
}

public void setPrice(Integer price) {
    this.price = price;
}

public Integer getStockQuantity() {
    return stockQuantity;
}

public void setStockQuantity(Integer stockQuantity) {
    this.stockQuantity = stockQuantity;
}

public Long getCategoryId() {
    return categoryId;
}

public void setCategoryId(Long categoryId) {
    this.categoryId = categoryId;
}

public String getImageUrl() {
    return imageUrl;
}

public void setImageUrl(String imageUrl) {
    this.imageUrl = imageUrl;
}

public String getDifficultyLevel() {
    return difficultyLevel;
}

public void setDifficultyLevel(String difficultyLevel) {
    this.difficultyLevel = difficultyLevel;
}

public String getAgeRange() {
    return ageRange;
}

public void setAgeRange(String ageRange) {
    this.ageRange = ageRange;
}

public Long getCreatedBy() {
    return createdBy;
}

public void setCreatedBy(Long createdBy) {
    this.createdBy = createdBy;
}

public List<Lab> getLabs() {
    return labs;
}

public void setLabs(List<Lab> labs) {
    this.labs = labs;
}


}

