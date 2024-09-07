package by.alst.jbdc.main;

import by.alst.jbdc.dao.MyJakartaDao;
import by.alst.jbdc.entity.MyJakarta;
import by.alst.jbdc.entity.Technology;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JdbcRunner {

    private static MyJakarta myJakarta;
    private final static MyJakartaDao myJakartaDao = MyJakartaDao.getInstance();

    static {
        myJakarta = myJakartaDao.readMyJakarta() != null
                ? myJakartaDao.readMyJakarta() : new MyJakarta();
    }


    public static void main(String[] args) {

        System.out.println("myJakarta из статического блока инициализации: ");
        System.out.println(myJakarta);
        System.out.println();

        Technology activation = new Technology();
        activation.setTechnologyName("Jakarta Activation 2.1");
        activation.setTechnologyDescription("Jakarta Activation defines a set of standard services to: determine" +
                                            " the MIME type of an arbitrary piece of data; encapsulate access to" +
                                            " it; discover the operations available on it; and instantiate the" +
                                            " appropriate bean to perform the operation(s).");

        Technology mail = new Technology();
        mail.setTechnologyName("Jakarta Mail 2.1");
        mail.setTechnologyDescription("The goal of this release is to provide standalone API jar fully independent" +
                                      " on the particular implementation.");

        List<Technology> technologyList = new ArrayList<>();
        technologyList.add(activation);
        technologyList.add(mail);

        myJakarta.setVersion("10");
        myJakarta.setDescription(" Eclipse Enterprise for Java (EE4J) is an open source initiative to create" +
                                 " standard APIs, implementations of those APIs, and technology compatibility" +
                                 " kits for Java runtimes that enable development, deployment, and management" +
                                 " of server-side and cloud-native applications.");
        myJakarta.setTechnologyList(technologyList);

        System.out.println("myJakarta перед записью в базу данных: ");
        System.out.println(myJakarta);
        System.out.println();

        myJakarta = myJakartaDao.writeMyJakarta(myJakarta);

        System.out.println("myJakarta записанная в базу данных: ");
        System.out.println(myJakarta);
        System.out.println();

        myJakarta = myJakartaDao.readMyJakarta();
        System.out.println("myJakarta прочитанная из базы данных: ");
        System.out.println(myJakarta);
        System.out.println();

        myJakarta.setVersion(null);
        System.out.println("myJakarta перед записью в базу данных: ");
        System.out.println(myJakarta);
        System.out.println();

        myJakarta = myJakartaDao.writeMyJakarta(myJakarta);

        System.out.println("myJakarta записанная в базу данных: ");
        System.out.println(myJakarta);
        System.out.println();

        myJakarta = myJakartaDao.readMyJakarta();
        System.out.println("myJakarta прочитанная из базы данных: ");
        System.out.println(myJakarta);
        System.out.println();


        Technology updateTechnology = myJakartaDao.readMyJakarta().getTechnologyList()
                .get(new Random().nextInt(0, myJakarta.getTechnologyList().size()));
        updateTechnology.setTechnologyDescription(updateTechnology.getTechnologyDescription() + " 2024");
        System.out.println("technology из myJakarta с корректировкой описания: ");
        System.out.println(updateTechnology);
        System.out.println();

        boolean updateResult = myJakartaDao.updateTechnology(updateTechnology);
        System.out.print("Результат updatetechnology: ");
        System.out.println(updateResult);
        System.out.println();

        Technology newTechnology = new Technology("Jakarta Annotations 2.1",
                "Jakarta Annotations defines a collection of annotations representing" +
                " common semantic concepts that enable a declarative style of programming that applies across" +
                " a variety of Java technologies.");

        System.out.println("technology новая для обновления: ");
        System.out.println(newTechnology);
        System.out.println();

        updateResult = myJakartaDao.updateTechnology(newTechnology);
        System.out.print("Результат updatetechnology: ");
        System.out.println(updateResult);
        System.out.println();

        newTechnology = null;
        System.out.println("technology новая для обновления: ");
        System.out.println(newTechnology);
        System.out.println();

        updateResult = myJakartaDao.updateTechnology(newTechnology);
        System.out.print("Результат updatetechnology: ");
        System.out.println(updateResult);
        System.out.println();

        newTechnology = new Technology(null, "null");
        System.out.println("technology новая для обновления: ");
        System.out.println(newTechnology);
        System.out.println();

        updateResult = myJakartaDao.updateTechnology(newTechnology);
        System.out.print("Результат updatetechnology: ");
        System.out.println(updateResult);
        System.out.println();

        System.out.println("Итоговая myJakarta: ");
        System.out.println(myJakartaDao.readMyJakarta());

        myJakartaDao.deleteMyJakarta();
    }
}