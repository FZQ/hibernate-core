package org.hibernate.test.instrument.cases;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.spi.ServiceRegistry;
import org.hibernate.testing.ServiceRegistryBuilder;

/**
 * @author Steve Ebersole
 */
public abstract class AbstractExecutable implements Executable {

	private ServiceRegistry serviceRegistry;
	private SessionFactory factory;

	public final void prepare() {
		Configuration cfg = new Configuration().setProperty( Environment.HBM2DDL_AUTO, "create-drop" );
		String[] resources = getResources();
		for ( String resource : resources ) {
			cfg.addResource( resource );
		}
		serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() );
		factory = cfg.buildSessionFactory( serviceRegistry );
	}

	public final void complete() {
		try {
			cleanup();
		}
		finally {
			factory.close();
			if ( serviceRegistry != null ) {
				ServiceRegistryBuilder.destroy( serviceRegistry );
			}
		}
	}

	protected SessionFactory getFactory() {
		return factory;
	}

	protected void cleanup() {
	}

	protected String[] getResources() {
		return new String[] { "org/hibernate/test/instrument/domain/Documents.hbm.xml" };
	}
}
