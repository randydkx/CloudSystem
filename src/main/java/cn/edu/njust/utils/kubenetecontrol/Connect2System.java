package cn.edu.njust.utils.kubenetecontrol;

import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.Configuration;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.V1Pod;
import io.kubernetes.client.openapi.models.V1PodList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

import java.io.FileReader;
import java.io.IOException;

public class Connect2System {
	static CoreV1Api api = null;
	public static ApiClient client=null;
	public static CoreV1Api getAPI() {
		if (api != null)
			return api;
		String kubeConfigPath = "C:\\config";

		// loading the out-of-cluster config, a kubeconfig from file-system
		try {
			client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
			// set the global default api-client to the in-cluster one from above
			Configuration.setDefaultApiClient(client);


			//client.setVerifyingSsl(false);
			// the CoreV1Api loads default api-client from global configuration.
			api = new CoreV1Api();
			// invokes the CoreV1Api client
			V1PodList list;
			try {
				list = api.listPodForAllNamespaces(null, null, null, null, null, null, null, null, null);
				for (V1Pod item : list.getItems()) {
					Log.printLine(item.getMetadata().getName());
				}
			} catch (ApiException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			Log.printLine("Connection established");
		} catch (IOException e) {//| ApiException
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return api;
	}
}
