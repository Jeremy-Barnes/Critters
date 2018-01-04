import com.critters.bll.UserBLL;
import com.critters.dal.HibernateUtil;
import com.critters.dal.dto.entity.User;
import junit.framework.TestCase;
import org.mockito.listeners.InvocationListener;
import org.mockito.listeners.MethodInvocationReport;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.lang.reflect.Field;

import static org.mockito.Mockito.*;

/**
 * Created by Jeremy on 1/1/2018.
 */

public class DALUnitTests extends TestCase {
	public boolean inATrxn = false;
	public boolean doubleTrxn = false;

	EntityManager mockmanager;
	EntityManagerFactory mockmanagerfactory;
	EntityTransaction mockTrxn;

	public void setUp() throws Exception {
		super.setUp();

		User user = mock(User.class);


		mockmanagerfactory = mock(EntityManagerFactory.class);
		mockmanager = mock(EntityManager.class);
		mockTrxn = mock(EntityTransaction.class, withSettings().invocationListeners(new InvocationListener() {
			@Override
			public void reportInvocation(MethodInvocationReport methodInvocationReport) {
				if(methodInvocationReport.getInvocation().toString().toUpperCase().contains(".begin()".toUpperCase())) {
					if(inATrxn) doubleTrxn = true;
					inATrxn = true;
				}
			}
		}));
		doAnswer(invocationOnMock -> true).when(mockmanager).isOpen();
		doAnswer(invocationOnMock1 -> mockTrxn).when(mockmanager).getTransaction();
		doAnswer(invocationOnMock -> true).when(mockTrxn).isActive();
		doAnswer(invocationOnMock -> true).when(mockTrxn).commit();
		when(mockmanagerfactory.createEntityManager()).thenReturn(mockmanager);

		mockmanagerfactory.createEntityManager().getTransaction().begin();
		System.out.println(doubleTrxn);
		mockmanagerfactory.createEntityManager().getTransaction().begin();
		System.out.println(doubleTrxn);

		Field field = HibernateUtil.class.getDeclaredField("entityManagerFactory");
		field.setAccessible(true);
		field.set(field, mockmanagerfactory);

	}

	public void testName() throws Exception {
		UserBLL.getUser("", "", true);
		assertTrue(true);//big if true
	}
}
