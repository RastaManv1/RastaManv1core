/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package logic;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс бизнесс логики
 *
 * @author Плахтий Александр Сергеевич
 */
public class RMCore
{

    private static RMCore instance = null;

    /**
     * Приватный конструктор
     *
     * Добавил Александр 18.05.2011 19:22
     */
    private RMCore()
    {
    }

    /**
     * Статический метод который создаёт объект
     *
     * @return RMCore
     * Добавил Александр 18.05.2011 19:22
     */

    public static RMCore getInstance()
    {
        if (instance == null)
        {
            instance = new RMCore();
            DB.getInstance().openConnection();
        }
        return instance;
    }

    /**
     * Проверка авторизации
     * @param String login логин пользователя (уникальный)
     * @param String paswd пароль для логина
     *
     * @return String имя пользователля который зарегестрирован под данным логином если операция прошла успешно
     * и такой пользователь существует, иначе null
     * Добавил Александр 18.05.2011 19:42
     */
    public String checkAccount(String login, String paswd)
    {
        String q = null;
        q = "select * from Account where login='" + login + "' and password='" + paswd + "'";
        DB.getInstance().query(q);
        if (DB.getInstance().getRowcount() == 0)
        {
            return null;//if don't found any then returns null
        }
        return DB.getInstance().getResultList().get(0).get("NAME").toString(); //if all OK returns name of user
    }

    /**
     * Получение приоритета пользователя
     *
     * @return int возвращает приоритет пользователя. (адекватно работает после вызова функции авторизаци
     *      @see checkAccount)
     *      в случаее ошибки возвращает -1
     * Добавил Александр 18.05.2011 20:16
     */
    public int getPriority()
    {
        try
        {
            return Integer.valueOf(DB.getInstance().getResultList().get(0).get("PRIORITY").toString());
        } catch (Exception e)
        {
            return -1;
        }
    }

    /**
     * Получение приоритета пользователя
     *
     * @return int возвращает приоритет пользователя, который зарегестрирован под указанным логином.
     *      в случаее ошибки возвращает -1
     * Добавил Александр 18.05.2011 20:16
     */
    public int getPriority(String login)
    {
        DB.getInstance().query("select priority from Account where login='" + login + "'");
        if (DB.getInstance().getRowcount() == 0)
        {
            return -1;
        }
        return Integer.valueOf(DB.getInstance().getResultList().get(0).get("PRIORITY").toString());
    }

    /**
     * Возвращает все существующиее ресурсы в системе
     *
     * @return ArrayList<HashMap>
     * Добавил Александр 19.05.2011 17:01
     */
    public ArrayList<HashMap> getResources()
    {
        if (!DB.getInstance().query("Select title from resources"))
        {
            return null;
        }
        return DB.getInstance().getResultList();
    }

    /**
     * Возвращает все существующиее роли в системе
     *
     * @return ArrayList<HashMap>
     * Добавил Александр 19.05.2011 17:03
     */
    public ArrayList<HashMap> getRoles()
    {
        if (!DB.getInstance().query("Select title from role"))
        {
            return null;
        }
        return DB.getInstance().getResultList();
    }

}
