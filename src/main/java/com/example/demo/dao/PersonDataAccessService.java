package com.example.demo.dao;


import com.example.demo.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class PersonDataAccessService implements PersonDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public PersonDataAccessService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public int insertPerson(UUID id, Person person) {
        final String sql = "INSERT INTO person (id,name) VALUES (?,?)";
        if (jdbcTemplate.update(sql, new Object[]{id, person.getName()}) != 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public List<Person> selectAllPeople() {
        final String sql = "SELECT id, name FROM person";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            UUID id = UUID.fromString(resultSet.getString("id"));
            String name = resultSet.getString("name");
            return new Person(id, name);
        });
    }

    @Override
    public int deletePerson(UUID id) {
        final String sql = "DELETE FROM person WHERE id=?";
        if (jdbcTemplate.update(sql, new Object[]{id}) != 0) {
            return 0;
        } else {
            return 1;
        }
    }

    @Override
    public int updatePersonById(UUID id, Person person) {
        final String sql = "UPDATE person SET name=? WHERE id=?";
        if (jdbcTemplate.update(sql, new Object[]{person.getName(), id}) != 0) {
            return 0;
        } else {
            return 1;
        }
    }


    @Override
    public Optional<Person> selectPersonById(UUID id) {
        final String sql = "SELECT id, name FROM person WHERE id = ?";
        Person person = jdbcTemplate.queryForObject(sql,
                new Object[]{id},
                (resultSet, i) -> {
                    UUID person_id = UUID.fromString(resultSet.getString("id"));
                    String name = resultSet.getString("name");
                    return new Person(person_id, name);
                });
        return Optional.ofNullable(person);
    }
}
