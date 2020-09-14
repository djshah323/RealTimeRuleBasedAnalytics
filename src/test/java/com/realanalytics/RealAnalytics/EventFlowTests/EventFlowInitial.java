package com.realanalytics.RealAnalytics.EventFlowTests;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.realanalytics.RealAnalytics.Applications.AppReferer;
import com.realanalytics.RealAnalytics.Applications.Events.ApplicationEvent;
import com.realanalytics.RealAnalytics.Applications.Events.AzureADEvent;
import com.realanalytics.RealAnalytics.Data.AnalyticEvent;
import com.realanalytics.RealAnalytics.Events.Utils;
import com.realanalytics.RealAnalytics.Events.Services.EventMapper;
import com.realanalytics.RealAnalytics.Events.Services.EventSanity;
import com.realanalytics.RealAnalytics.Exceptions.BadEventException;
import com.realanalytics.RealAnalytics.Exceptions.IllegalAppNameException;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class EventFlowInitial {

	@Autowired
	private EventMapper eventMapper;
	
	@Autowired
	private EventSanity<?> eventSanity;
	
	
	@Test
	void appFetchTest() {
		try {
			assertEquals(AppReferer.AzureAD, Utils.getAppDetails("azuread"));
			assertEquals(AppReferer.Google, Utils.getAppDetails("google"));
			Utils.getAppDetails("BadName");
		} catch (IllegalAppNameException e) {
			assert true;
		}
	}
	
	@Test
	ApplicationEvent 
	appEventGenTest() {
		String strEvent = "{ \"CreationTime\": \"2020-09-09T01:54:38\","
				+ " \"Id\": \"9a63369e-22e0-4683-8989-4d99e8a203f0\", "
				+ "\"Operation\": \"UserLoginFailed\", "
				+ "\"OrganizationId\": \"f6aa9224-6487-4d38-a06a-0b0aea9d9998\", "
				+ "\"RecordType\": 15, "
				+ "\"ResultStatus\": \"Failed\", "
				+ "\"UserKey\": \"1003BFFD8A23B072@sumoskope.onmicrosoft.com\", "
				+ "\"UserType\": 0, \"Version\": 1, "
				+ "\"Workload\": \"AzureActiveDirectory\", "
				+ "\"ClientIP\": \"70.42.129.126\", \"ObjectId\": \"Unknown\", "
				+ "\"UserId\": \"admin@sumoskope.onmicrosoft.com\","
				+ " \"AzureActiveDirectoryEventType\": 1, "
				+ "\"ExtendedProperties\": [{ \"Name\": \"UserAgent\", \"Value\": \"python-requests/2.13.0\" }, "
						+ "{ \"Name\": \"RequestType\", \"Value\": \"OrgIdWsTrust2:extsts\" }, "
						+ "{ \"Name\": \"ResultStatusDetail\", \"Value\": \"UserError\" }], "
				+ "\"ModifiedProperties\": [], "
				+ "\"Actor\": [{ \"ID\": \"0326a094-de82-4cc1-b711-345bcc5ae22c\", \"Type\": 0 }, "
						+ "{ \"ID\": \"admin@sumoskope.onmicrosoft.com\", \"Type\": 5 }, "
						+ "{ \"ID\": \"1003BFFD8A23B072\", \"Type\": 3 }], "
				+ "\"ActorContextId\": \"f6aa9224-6487-4d38-a06a-0b0aea9d9998\", "
				+ "\"ActorIpAddress\": \"70.42.129.126\", "
				+ "\"InterSystemsId\": \"7d199409-329f-408e-98ea-c2a942fadfc4\", "
				+ "\"IntraSystemId\": \"ab0789f7-6a46-4156-a16a-a9324e491a00\", "
				+ "\"SupportTicketId\": \"\", "
				+ "\"Target\": [{ \"ID\": \"Unknown\", \"Type\": 0 }], "
				+ "\"TargetContextId\": \"f6aa9224-6487-4d38-a06a-0b0aea9d9998\", "
				+ "\"ApplicationId\": \"00000003-0000-0ff1-ce00-000000000000\", "
				+ "\"LogonError\": \"InvalidUserNameOrPassword\" }";
		try {
			ApplicationEvent appEvent =
					eventSanity.check(AppReferer.AzureAD, strEvent);
			if (appEvent instanceof AzureADEvent) {
				assert true;
			} else {
				assert false;
			}
			return appEvent;
		} catch (BadEventException e) {
			// TODO Auto-generated catch block
			assert false;
		}
		return null;
	}
	
	@Test
	void checkAppEventTest() {
		AzureADEvent ev = (AzureADEvent) appEventGenTest();
		assertEquals("9a63369e-22e0-4683-8989-4d99e8a203f0", ev.Id);
		assertEquals("f6aa9224-6487-4d38-a06a-0b0aea9d9998", ev.ActorContextId);
		assertEquals("00000003-0000-0ff1-ce00-000000000000", ev.ApplicationId);
		assertEquals("70.42.129.126", ev.ClientIP);
		assertEquals("1", ev.Version);
		assertEquals("2020-09-09T01:54:38", ev.CreationTime);
		assertEquals("UserLoginFailed", ev.Operation);
		assertEquals("Unknown", ev.ObjectId);
		assertEquals("admin@sumoskope.onmicrosoft.com", ev.UserId);
		assertEquals("f6aa9224-6487-4d38-a06a-0b0aea9d9998", ev.OrganizationId);
	}
	
	@Test
	void appToAnalyticEventTest() {
		ApplicationEvent appEvent = appEventGenTest();
		AnalyticEvent aEv = eventMapper.createAnalyticEvent(appEvent);
		assertEquals( "AzureAD", aEv.getApplicationName());
		assertEquals("WsTrust", aEv.getDeviceAuth());
		assertEquals("python-requests/2.13.0", aEv.getDeviceName());
		assertEquals("Browser", aEv.getDeviceType());
		assertEquals("UserLoginFailed", aEv.getEventAction());
		assertEquals("2020-09-09T01:54:38", aEv.getEventDate());
		assertEquals("United States", aEv.getGeoIP().getCountry());
		assertEquals("70.42.129.126", aEv.getSrcIp());
		assertEquals("Failed", aEv.getStatus());
		
	}
}
