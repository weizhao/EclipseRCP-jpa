package org.eclipse.ui.examples.contributions.model.setup;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.jpa.osgi.PersistenceProvider;
import org.eclipse.ui.examples.contributions.model.annotated.Person;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class Activator implements BundleActivator {

	private static BundleContext context;

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext context) throws Exception {
		EntityManagerFactory emf = null;
		EntityManager em = null;
		try {
			Map<String, Object> properties = new HashMap<String, Object>();
			properties.put("eclipselink.ddl-generation", "drop-and-create-tables");
			properties.put("eclipselink.ddl-generation.output-mode", "database");
			properties.put("eclipselink.classloader", this.getClass().getClassLoader());
			emf = new PersistenceProvider().createEntityManagerFactory("person", properties);
			em = emf.createEntityManager();
			em.getTransaction().begin();

			URL personFileURL = context.getBundle().getResource("person.tab");
			Map<Integer, Person> persons = loadPersons(personFileURL);
			persist(em, persons.values());

			// update DEFAULT sequence to highest used PK
			List<Integer> allIds = new ArrayList<Integer>();
			allIds.addAll(persons.keySet());
			Collections.sort(allIds);
			Integer highestId = allIds.get(allIds.size() - 1);
			String sequenceSql = "UPDATE SEQUENCE SET SEQ_COUNT=" + ++highestId + " WHERE SEQ_NAME = 'SEQ_GEN'";
			em.createNativeQuery(sequenceSql).executeUpdate();

			em.getTransaction().commit();
		} finally {
			if (em != null) {
				em.close();
			}
			if (emf != null) {
				emf.close();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		Activator.context = null;
	}

	private static void persist(EntityManager em, Collection<?> values) {
		for (Object object : values) {
			em.persist(object);
		}
	}

	private static Map<Integer, Person> loadPersons(URL fileURL) throws Exception {
		Map<Integer, Person> persons = new HashMap<Integer, Person>();
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(fileURL.openStream()));
			String nextLine = null;
			while ((nextLine = reader.readLine()) != null) {
				Person person = buildPerson(nextLine);
				persons.put(person.getId(), person);
			}
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return persons;
	}

	private static Person buildPerson(String line) {
		// NAME ID
		String[] columns = line.split("\t");
		assert columns.length == 2;
		Person person = new Person(Integer.valueOf(columns[1]), columns[0], columns[0] + "_given");
		return person;
	}

}
