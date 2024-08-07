- **JDBC:** Java database connectivity
- **JPA:** Java persistent API    
- JPA is standard for doing object relational mapping. (mapping of objects to tables)
- think of JPA as an interface and Hibernate is implementation.
- If there is in memory db, then hibernate default feature of schema update will create the table in db.
- JPQL:java persistence query language
- **Annotations:** 
  - @Entity: to create Entity   
  - @TABLE 
  - @Column 
  - @Id: used to define the field as primary key 
  - @GeneratedValue: auto generated value by the framework:JPA
- JPA needs default noArg constructor 

- **EntityManager** 
- em: EntityManager is simple interface to PersistenceContext.
  - em methods:
    - find(Class<T> var1, identifier): returns the object T.
- Spring autoconfiguration-> H2 db ->hibernate auto enable schema-> created tables-> beans annotated with @Entity.
- JPQL: Java persistence query language
  - In SQl we query from the db, in JPQL  we query from Entities. JPQL queries are converted into sql queries by JPA implementation i.e Hibernate
- NamedQuery: Assign name to query for re-usability purpose.

- When to go for native queries:
    - when you want to use some feature of db which is not supported by JPA, mass update, tuning features.
- Eager Fetching:
    - one to one relationship , when we pull the one entity other one also pulled automatically.
    - this can be performance hit.
- Lazy Fetching:
    -  put fetch type at entity mapping : **@OneToOne(fetch = FetchType.LAZY)**

      - Transaction:
        public void someTest()
            {
                //Database operation 1: Retrieve student
                Student student = em.find(Student.class,20001);
                //Persistence Context(student)

                //Database operation 2: Retrieve passport
                Passport passport=student.getPassport();
                //Persistence Context(student, passport)
        
                //Database operation 3: update passport
                passport.setNumber("G2389");
                //Persistence Context(student, passport++)
        
                //Database operation 4: update student
                student.setName("Navneet-updated");
                //Persistence Context(student++, passport++)
        
            }

- transaction will be sent to db at the end of the block.
- Persistence Context
  - To temp store entities we are operating in
  - To give access to db
- Entity Manager -> Persistence Context -> DB
- In JPA we interact with Persistence context through Entity Manager Interface.
- If there is no @Transactional on method then transaction is created for the individual db query and will be closed just after it.
- In Hibernate
  - session= Persistence Context
  - session Factory = EntityManager
- Persistence Context is created at the start of transaction and is closed at the end of transaction.
- Owning side of relation: Entity which has the identifier stored of the other Entity, having relation with.
- Uni-directional : When we can pull details of related entity from the owning entity only.
- Bi-directional relation: When we can pull details of either entity either through owning entity or owned entity

- @OneToOne
  - default fetch strategy is eager

- Put **mappedBy** in Entity which doesn't own the relationship: **@OneToOne(fetch = FetchType.LAZY, mappedBy = "name of the field of owning side through which it is mapped to other entity")**
- @OneToMany
  - default fetch strategy is Lazy: meaning if one entity has one to many relation with another entity, if we pull the entity with 1:M then only that entity will be pulled default
- @ManyToOne
  - default fetch strategy is Eager: meaning if one entity has many to one relation with another entity, if we pull the entity with M:1 then  entity will pull the other entity as well
- @ManyToMany
  - default fetch strategy is Lazy
  - @JoinTable(name = "mapping table name", joinColumns = @JoinColumn(name = "owning side column name"), inverseJoinColumns = @JoinColumn(name = "other side of column name"))
  - @JoinTable will be put on owning side
- **While setting the values in DB owning side should have the relationship defined (add reference of other into owning side  in java entity being passed to save in db)in java  code then only mapping will be set in DB**
- **Inheritance relationships mapping**
  - On Entity put **@Inheritance(strategy = InheritanceType.SINGLE_TABLE)** : performance is needed then single table is good option
    - if we want all the subclasses to be stored in single table
    - Performance would be better as we need to query from single table, but we would have more nullables in db columns
    - Data integrity is not good as we need keep the columns nullable to accept null value
    - DTYPE column will be added automatically, it stores the subclassed entity type.
    - To rename the name DTYPE(DiscriminatorType) we can use @DiscriminatorColumn(name="column_name")
  - **@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)**: repeating columns so not good design
    - separate table for each concrete subclass of entity
    - common columns are repeated in all tables
  - **@Inheritance(strategy = InheritanceType.JOINED)** : data integrity is good, all columns can be non nullable no chance to bad data
    - every class will have separate table including parent and child classes. Rows are mapped using foreign keys contraints
    - parent class will have common columns and child classes will have specific columns
    - good in terms schema design no duplication data
    - performance hit will be there are joins need to perform to pull data from all associated tables
  - **@MappedSuperclass**: repeating columns not good design
    - No inheritance is used, each subclasses has its own table like as TABLE_PER_CLASS but here Parent entity is not present
    - Here we need to pull the specific concrete  entities , superclass exists only for the holding of common properties, but will not be an entity like in the case of TABLE_PER_CLASS
