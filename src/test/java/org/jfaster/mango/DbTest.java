/*
 * Copyright 2014 mango.jfaster.org
 *
 * The Mango Project licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package org.jfaster.mango;

import org.jfaster.mango.annotation.DB;
import org.jfaster.mango.annotation.Rename;
import org.jfaster.mango.annotation.ReturnGeneratedId;
import org.jfaster.mango.annotation.SQL;
import org.jfaster.mango.operator.Mango;
import org.jfaster.mango.support.Randoms;
import org.jfaster.mango.support.Table;
import org.jfaster.mango.support.model4table.User;
import org.jfaster.mango.support.Config;
import com.google.common.primitives.Ints;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.Connection;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * 测试db
 *
 * @author ash
 */
public class DbTest {

    private final static DataSource ds = Config.getDataSource();
    private final static Mango mango = new Mango(ds);
    private final static UserDao dao = mango.create(UserDao.class);

    @Before
    public void before() throws Exception {
        Connection conn = ds.getConnection();
        Table.USER.load(conn);
        conn.close();
    }

/*********************************测试查询开始*************************************/

    @Test
    public void testQueryInteger() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getIntegerId(id), equalTo(id));
    }

    @Test
    public void testQueryInt() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getIntId(id), equalTo(id));
    }

    @Test
    public void testQueryString() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getName(id), equalTo(user.getName()));
    }

    @Test
    public void testQueryBooleanObj() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getBoolObjGender(id), equalTo(user.isGender()));
    }

    @Test
    public void testQueryBoolean() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getBoolGender(id), equalTo(user.isGender()));
    }

    @Test
    public void testQueryLongObj() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getLongObjMoney(id), equalTo(user.getMoney()));
    }

    @Test
    public void testQueryLong() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getLongMoney(id), equalTo(user.getMoney()));
    }

    @Test
    public void testQueryDate() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        assertThat(dao.getDate(id).getTime(), equalTo(user.getUpdateTime().getTime()));
    }

    @Test
    public void testQueryUser() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        user.setId(id);
        assertThat(dao.getUser(id), equalTo(user));
    }

    @Test
    public void testQueryUserList() throws Exception {
        List<User> users = createRandomUsers(10);
        for (User user : users) {
            int id = dao.insertUser(user);
            user.setId(id);
        }
        List<User> actual = dao.getUserList();
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, contains(users.toArray()));
    }

    @Test
    public void testQueryUserSet() throws Exception {
        List<User> users = createRandomUsers(10);
        for (User user : users) {
            int id = dao.insertUser(user);
            user.setId(id);
        }
        Set<User> actual = dao.getUserSet();
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void testQueryUserArray() throws Exception {
        List<User> users = createRandomUsers(10);
        for (User user : users) {
            int id = dao.insertUser(user);
            user.setId(id);
        }
        List<User> actual = Arrays.asList(dao.getUserArray());
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, contains(users.toArray()));
    }

    @Test
    public void testQueryIntegerArray() throws Exception {
        List<Integer> ids = new ArrayList<Integer>();
        for (User user : createRandomUsers(10)) {
            int id = dao.insertUser(user);
            ids.add(id);
        }
        List<Integer> actual = Arrays.asList(dao.getIntegerArray());
        assertThat(actual, hasSize(ids.size()));
        assertThat(actual, contains(ids.toArray()));
    }

    @Test
    public void testQueryIntArray() throws Exception {
        List<Integer> ids = new ArrayList<Integer>();
        for (User user : createRandomUsers(10)) {
            int id = dao.insertUser(user);
            ids.add(id);
        }
        List<Integer> actual = Ints.asList(dao.getIntArray());
        assertThat(actual, hasSize(ids.size()));
        assertThat(actual, contains(ids.toArray()));
    }

    @Test
    public void testQeuryInList() throws Exception {
        List<User> users = createRandomUsers(10);
        List<Integer> ids = new ArrayList<Integer>();
        for (User user : users) {
            int id = dao.insertUser(user);
            ids.add(id);
            user.setId(id);
        }
        List<User> actual = dao.getUsersInList(ids);
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void testQueryInSet() throws Exception {
        List<User> users = createRandomUsers(10);
        Set<Integer> ids = new HashSet<Integer>();
        for (User user : users) {
            int id = dao.insertUser(user);
            ids.add(id);
            user.setId(id);
        }
        List<User> actual = dao.getUsersInSet(ids);
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void testQueryInIntegerArray() throws Exception {
        List<User> users = createRandomUsers(10);
        Integer[] ids = new Integer[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int id = dao.insertUser(user);
            ids[i] = id;
            user.setId(id);
        }
        List<User> actual = dao.getUsersInIntegerArray(ids);
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void testQueryInIntArray() throws Exception {
        List<User> users = createRandomUsers(10);
        int[] ids = new int[users.size()];
        for (int i = 0; i < users.size(); i++) {
            User user = users.get(i);
            int id = dao.insertUser(user);
            ids[i] = id;
            user.setId(id);
        }
        List<User> actual = dao.getUsersInIntArray(ids);
        assertThat(actual, hasSize(users.size()));
        assertThat(actual, containsInAnyOrder(users.toArray()));
    }


/********************************测试更新开始***************************************/

    @Test
    public void testInsert() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user.getName(), user.getAge(), user.isGender(), user.getMoney(), user.getUpdateTime());
        user.setId(id);
        assertThat(dao.getUser(id), equalTo(user));
    }


    @Test
    public void testUpdate() throws Exception {
        User user = createRandomUser();
        int id = dao.insertUser(user);
        user.setId(id);
        User user2 = dao.getUser(id);
        assertThat(user2, equalTo(user));
        User user3 = createRandomUser();
        user3.setId(id);
        int r = dao.updateUser(user3);
        assertThat(r, equalTo(1));
        User user4 = dao.getUser(id);
        assertThat(user4, equalTo(user3));
        r = dao.deleteUser(id);
        assertThat(r, equalTo(1));
        User user5 = dao.getUser(id);
        assertThat(user5, equalTo(null));
    }

    @Test
    public void testUpdateSelectNull() throws Exception {
        User user = createRandomUser();
        user.setName(null);
        user.setUpdateTime(null);
        int id = dao.insertUser(user);
        user.setId(id);
        User user2 = dao.getUser(id);
        assertThat(user2, equalTo(user));
        String r = dao.getName(id);
        assertThat(r, equalTo(null));
        user.setMoney(null);
        int r2 = dao.updateUser(user);
        assertThat(r2, equalTo(1));
        Long r3 = dao.getLongObjMoney(id);
        assertThat(r3, equalTo(null));
    }

/********************************测试批量更新开始***************************************/

    @Test
    public void testBatchUpdateList() throws Exception {
        List<User> users = createRandomUsers(20);
        int age = 10086;
        for (User user : users) {
            user.setAge(age);
        }
        int[] r = dao.batchInsertUserList(users);
        assertThat(r.length, equalTo(users.size()));
        assertThat(Ints.asList(r), everyItem(equalTo(1)));
        List<User> actual = dao.getUsersByAge(age);
        assertThat(actual, hasSize(users.size()));
        for (int i = 0; i < users.size(); i++) {
            actual.get(i).setId(0);
        }
        assertThat(actual, contains(users.toArray()));
    }

    @Test
    public void testBatchUpdateSet() throws Exception {
        Set<User> users = new HashSet<User>(createRandomUsers(20));
        int age = 10086;
        for (User user : users) {
            user.setAge(age);
        }
        int[] r = dao.batchInsertUserSet(users);
        assertThat(r.length, equalTo(users.size()));
        assertThat(Ints.asList(r), everyItem(equalTo(1)));
        List<User> actual = dao.getUsersByAge(age);
        assertThat(actual, hasSize(users.size()));
        for (int i = 0; i < users.size(); i++) {
            actual.get(i).setId(0);
        }
        assertThat(actual, containsInAnyOrder(users.toArray()));
    }

    @Test
    public void testBatchUpdateArray() throws Exception {
        User[] users = createRandomUsers(20).toArray(new User[0]);
        int age = 10086;
        for (User user : users) {
            user.setAge(age);
        }
        int[] r = dao.batchInsertUserArray(users);
        assertThat(r.length, equalTo(users.length));
        assertThat(Ints.asList(r), everyItem(equalTo(1)));
        List<User> actual = dao.getUsersByAge(age);
        assertThat(actual, hasSize(users.length));
        for (int i = 0; i < users.length; i++) {
            actual.get(i).setId(0);
        }
        assertThat(actual, contains(users));
    }

    private User createRandomUser() {
        Random r = new Random();
        String name = Randoms.randomString(20);
        int age = r.nextInt(200);
        boolean gender = r.nextBoolean();
        long money = r.nextInt(1000000);
        Date date = new Date();
        User user = new User(name, age, gender, money, date);
        return user;
    }

    private List<User> createRandomUsers(int size) {
        List<User> users = new ArrayList<User>(size);
        for (int i = 0; i < size; i++) {
            users.add(createRandomUser());
        }
        return users;
    }

    @DB()
    static interface UserDao {

        @SQL("select id from user where id = :1")
        public Integer getIntegerId(int id);

        @SQL("select id from user where id = :1")
        public int getIntId(int id);

        @SQL("select name from user where id = :1")
        public String getName(int id);

        @SQL("select gender from user where id = :1")
        public Boolean getBoolObjGender(int id);

        @SQL("select gender from user where id = :1")
        public boolean getBoolGender(int id);

        @SQL("select money from user where id = :1")
        public Long getLongObjMoney(int id);

        @SQL("select money from user where id = :1")
        public long getLongMoney(int id);

        @SQL("select update_time from user where id = :1")
        public Date getDate(int id);

        @SQL("select id, name, age, gender, money, update_time from user where id = :1")
        public User getUser(int id);

        @SQL("select id, name, age, gender, money, update_time from user order by id")
        public List<User> getUserList();

        @SQL("select id, name, age, gender, money, update_time from user order by id")
        public Set<User> getUserSet();

        @SQL("select id, name, age, gender, money, update_time from user order by id")
        public User[] getUserArray();

        @SQL("select id from user order by id")
        public Integer[] getIntegerArray();

        @SQL("select id from user order by id")
        public int[] getIntArray();

        @SQL("select id, name, age, gender, money, update_time from user where id in (:1)")
        public List<User> getUsersInList(List<Integer> ids);

        @SQL("select id, name, age, gender, money, update_time from user where id in (:1)")
        public List<User> getUsersInSet(Set<Integer> ids);

        @SQL("select id, name, age, gender, money, update_time from user where id in (:1)")
        public List<User> getUsersInIntegerArray(Integer[] ids);

        @SQL("select id, name, age, gender, money, update_time from user where id in (:1)")
        public List<User> getUsersInIntArray(int[] ids);

        @SQL("select id, name, age, gender, money, update_time from user where age=:1 order by id")
        public List<User> getUsersByAge(int age);

        @SQL("select max(id) from user")
        public int getMaxInt();

        /***********************************************************************/

        @ReturnGeneratedId
        @SQL("insert into user(name, age, gender, money, update_time) values(:1, :2, :3, :4, :5)")
        public int insertUser(String name, int age, boolean gender, long money, Date updateTime);

        @ReturnGeneratedId
        @SQL("insert into user(name, age, gender, money, update_time) " +
                "values(:1.name, :1.age, :1.gender, :1.money, :1.updateTime)")
        public int insertUser(User user);

        @SQL("update user set name=:u.name, age=:u.age, gender=:u.gender, money=:u.money, update_time=:u.updateTime where id=:u.id")
        public int updateUser(@Rename("u") User user);

        @SQL("delete from user where id=:1")
        public int deleteUser(int id);

        @SQL("delete from user where age=:1")
        public int deleteUserByAge(int age);

        /***********************************************************************/

        @SQL("insert into user(name, age, gender, money, update_time) " +
                "values(:1.name, :1.age, :1.gender, :1.money, :1.updateTime)")
        public int[] batchInsertUserList(List<User> userList);

        @SQL("insert into user(name, age, gender, money, update_time) values(:1.name, :1.age, :1.gender, :1.money, :1.updateTime)")
        public int[] batchInsertUserSet(Set<User> userSet);

        @SQL("insert into user(name, age, gender, money, update_time) values(:1.name, :1.age, :1.gender, :1.money, :1.updateTime)")
        public int[] batchInsertUserArray(User[] users);

    }

}
