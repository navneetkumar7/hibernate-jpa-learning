package com.nt.database.jdbc;

import com.nt.database.jdbc.entity.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class PersonJdbcDAO {
    @Autowired
    JdbcTemplate jdbcTemplate;
    /*
    We have used BeanPropertyRowMapper because the name of the columns matches in entity class,
    if we have different table definition from the entity beans then
    we can create our custom row mapper
     */
    class PersonRowMapper implements RowMapper<Person>{
        @Override
        public Person mapRow(ResultSet rs, int rowNum) throws SQLException {
            Person person= new Person();
            person.setId(rs.getInt("id"));
            person.setName(rs.getString("name"));
            person.setLocation(rs.getString("location"));
            person.setBirthDate(rs.getTimestamp("birth_date"));
            return person;
        }
    }

    public List<Person> findAll(){
        return jdbcTemplate.query("select * from person", new BeanPropertyRowMapper<>(Person.class));
    }
    public List<Person> findAllWithCustomRowMapper(){
        return jdbcTemplate.query("select * from person", new PersonRowMapper());
    }
    public Person findById(int id){
        return jdbcTemplate.queryForObject("select * from person where id=?",new Object[]{id}, new BeanPropertyRowMapper<>(Person.class));
    }

    public List<Person> findByName(String name)
    {
        return jdbcTemplate.query("select * from person where name=?", new Object[]{name},new BeanPropertyRowMapper<>(Person.class));
    }

    public int deleteById(int id)
    {
        return jdbcTemplate.update("delete from person where id=?", new Object[]{id});
    }
    public int insert(Person person)
    {
        return jdbcTemplate.update
                ("insert into person (id, name, location, birth_date ) values(?,?,?,?)",
                        new Object[]{person.getId(), person.getName(), person.getLocation(), new Timestamp(person.getBirthDate().getTime())});
    }

    public int update(Person person)
    {
        return jdbcTemplate.update
                ("update person set name=?, location=?, birth_date=? where id= ?",
                        new Object[]{person.getName(), person.getLocation(), new Timestamp(person.getBirthDate().getTime()), person.getId()});
    }
}
