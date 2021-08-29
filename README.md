# CloudSystem
>1.使用set和get方法处理entity，否则无法解析完成json数据格式和
>javaObject之间的转换。

### infoServiceTest
```
NodeInfo{address='192.168.7.10', name='kube-1', coreNum=2, role='master', usage=Usage{CPUAmountStr='850m', CPUAmount=850.0, CPURatio=42.0, memoryStr='190Mi', memory=19.0, memoryRatio=11.0, Disk=38.28, diskRatio=0.1}}
NodeInfo{address='192.168.7.20', name='node-1', coreNum=2, role='node', usage=Usage{CPUAmountStr='700m', CPUAmount=700.0, CPURatio=35.0, memoryStr='396Mi', memory=39.0, memoryRatio=23.0, Disk=38.28, diskRatio=0.1}}
NodeInfo{address='192.168.7.30', name='node-2', coreNum=2, role='node', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='50Mi', memory=50.0, memoryRatio=5.0, Disk=38.28, diskRatio=0.1}}

Deployment{name='hello-node', ready='0/1', upToDate=1, available=0, namespace='default'}
Deployment{name='rubis-deployment', ready='1/1', upToDate=1, available=1, namespace='default'}
Deployment{name='nginx-ingress-controller', ready='1/1', upToDate=1, available=1, namespace='ingress-nginx'}
Deployment{name='coredns', ready='2/2', upToDate=2, available=2, namespace='kube-system'}

ContainerInfo{name='k8s_kube-scheduler_kube-scheduler-kube-1_kube-system_ca2aa1b3224c37fa1791ef6c7d883bbe_101', image='a31f78c7c8ce'}
ContainerInfo{name='k8s_kube-controller-manager_kube-controller-manager-kube-1_kube-system_8c682dfe8700d6651de5c6749c04c7be_102', image='d3e55153f52f'}
ContainerInfo{name='k8s_kube-scheduler_kube-scheduler-kube-1_kube-system_ca2aa1b3224c37fa1791ef6c7d883bbe_100', image='a31f78c7c8ce'}
ContainerInfo{name='k8s_kube-controller-manager_kube-controller-manager-kube-1_kube-system_8c682dfe8700d6651de5c6749c04c7be_101', image='d3e55153f52f'}
ContainerInfo{name='k8s_etcd_etcd-kube-1_kube-system_0955937c7d2477d4c37a1d7ffce15e21_26', image='303ce5db0e90'}
ContainerInfo{name='k8s_kube-apiserver_kube-apiserver-kube-1_kube-system_1c832f80b3c47b616f88740467089b3e_22', image='74060cea7f70'}
ContainerInfo{name='k8s_etcd_etcd-kube-1_kube-system_0955937c7d2477d4c37a1d7ffce15e21_25', image='303ce5db0e90'}
ContainerInfo{name='k8s_kube-apiserver_kube-apiserver-kube-1_kube-system_1c832f80b3c47b616f88740467089b3e_21', image='74060cea7f70'}
ContainerInfo{name='k8s_coredns_coredns-7ff77c879f-84bzc_kube-system_9d49f9bb-304c-4221-ab69-147026dbd07e_8', image='67da37a9a360'}
ContainerInfo{name='k8s_coredns_coredns-7ff77c879f-l8tmp_kube-system_518b2961-94da-4021-897e-ce6e14476cc8_8', image='67da37a9a360'}
ContainerInfo{name='k8s_POD_coredns-7ff77c879f-84bzc_kube-system_9d49f9bb-304c-4221-ab69-147026dbd07e_31', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_coredns-7ff77c879f-l8tmp_kube-system_518b2961-94da-4021-897e-ce6e14476cc8_31', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_kube-flannel_kube-flannel-ds-amd64-9bpc9_kube-system_ab97bcca-4f5a-47da-9769-715f58598fea_7', image='ff281650a721'}
ContainerInfo{name='k8s_install-cni_kube-flannel-ds-amd64-9bpc9_kube-system_ab97bcca-4f5a-47da-9769-715f58598fea_6', image='ff281650a721'}
ContainerInfo{name='k8s_POD_kube-flannel-ds-amd64-9bpc9_kube-system_ab97bcca-4f5a-47da-9769-715f58598fea_6', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_kube-proxy_kube-proxy-pc52q_kube-system_1e13d250-67cb-4076-a8fe-6329955b2937_8', image='43940c34f24f'}
ContainerInfo{name='k8s_POD_kube-proxy-pc52q_kube-system_1e13d250-67cb-4076-a8fe-6329955b2937_8', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_kube-apiserver-kube-1_kube-system_1c832f80b3c47b616f88740467089b3e_8', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_etcd-kube-1_kube-system_0955937c7d2477d4c37a1d7ffce15e21_8', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_kube-controller-manager-kube-1_kube-system_8c682dfe8700d6651de5c6749c04c7be_8', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_kube-scheduler-kube-1_kube-system_ca2aa1b3224c37fa1791ef6c7d883bbe_8', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_coredns_coredns-7ff77c879f-l8tmp_kube-system_518b2961-94da-4021-897e-ce6e14476cc8_7', image='67da37a9a360'}
ContainerInfo{name='k8s_coredns_coredns-7ff77c879f-84bzc_kube-system_9d49f9bb-304c-4221-ab69-147026dbd07e_7', image='67da37a9a360'}
ContainerInfo{name='k8s_POD_coredns-7ff77c879f-l8tmp_kube-system_518b2961-94da-4021-897e-ce6e14476cc8_26', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_coredns-7ff77c879f-84bzc_kube-system_9d49f9bb-304c-4221-ab69-147026dbd07e_26', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_kube-flannel_kube-flannel-ds-amd64-9bpc9_kube-system_ab97bcca-4f5a-47da-9769-715f58598fea_6', image='ff281650a721'}
ContainerInfo{name='k8s_kube-proxy_kube-proxy-pc52q_kube-system_1e13d250-67cb-4076-a8fe-6329955b2937_7', image='43940c34f24f'}
ContainerInfo{name='k8s_POD_kube-proxy-pc52q_kube-system_1e13d250-67cb-4076-a8fe-6329955b2937_7', image='registry.aliyuncs.com/google_containers/pause:3.2'}
ContainerInfo{name='k8s_POD_kube-flannel-ds-amd64-9bpc9_kube-system_ab97bcca-4f5a-47da-9769-715f58598fea_5', image='registry.aliyuncs.com/google_containers/pause:3.2'}

PodInfo{namespace='default', name='hello-node-8478cd7769-8r8lz', status='Pending', age='20d', containers='hello-node', usage=Usage{CPUAmountStr='0m', CPUAmount=0.0, CPURatio=0.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='default', name='hello-node-8478cd7769-tqhb5', status='Pending', age='2021-08-01T11:43:58.000+08:00', containers='hello-node', usage=null}
PodInfo{namespace='default', name='rubis-deployment-5f58f8d8cb-b848b', status='Running', age='20d', containers='rubis-pod', usage=Usage{CPUAmountStr='500m', CPUAmount=500.0, CPURatio=25.0, memoryStr='256Mi', memory=256.0, memoryRatio=14.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='ingress-nginx', name='nginx-ingress-controller-bb455fd47-2z7w2', status='Running', age='20d', containers='nginx-ingress-controller', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='90Mi', memory=90.0, memoryRatio=5.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='coredns-7ff77c879f-84bzc', status='Running', age='213d', containers='coredns', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='70Mi', memory=70.0, memoryRatio=4.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='coredns-7ff77c879f-l8tmp', status='Running', age='213d', containers='coredns', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='70Mi', memory=70.0, memoryRatio=4.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='etcd-kube-1', status='Running', age='213d', containers='etcd', usage=Usage{CPUAmountStr='0m', CPUAmount=0.0, CPURatio=0.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-apiserver-kube-1', status='Running', age='213d', containers='kube-apiserver', usage=Usage{CPUAmountStr='250m', CPUAmount=250.0, CPURatio=12.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-controller-manager-kube-1', status='Running', age='213d', containers='kube-controller-manager', usage=Usage{CPUAmountStr='200m', CPUAmount=200.0, CPURatio=10.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-flannel-ds-amd64-9bpc9', status='Running', age='67d', containers='kube-flannel', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='50Mi', memory=50.0, memoryRatio=2.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-flannel-ds-amd64-n8tnk', status='Running', age='67d', containers='kube-flannel', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='50Mi', memory=50.0, memoryRatio=2.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-flannel-ds-amd64-zlphc', status='Running', age='2021-08-01T11:39:31.000+08:00', containers='kube-flannel', usage=null}
PodInfo{namespace='kube-system', name='kube-proxy-lp8c7', status='Running', age='213d', containers='kube-proxy', usage=Usage{CPUAmountStr='0m', CPUAmount=0.0, CPURatio=0.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-proxy-pc52q', status='Running', age='213d', containers='kube-proxy', usage=Usage{CPUAmountStr='0m', CPUAmount=0.0, CPURatio=0.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
PodInfo{namespace='kube-system', name='kube-proxy-vp2nn', status='Running', age='2021-01-28T10:34:11.000+08:00', containers='kube-proxy', usage=null}
PodInfo{namespace='kube-system', name='kube-scheduler-kube-1', status='Running', age='213d', containers='kube-scheduler', usage=Usage{CPUAmountStr='100m', CPUAmount=100.0, CPURatio=5.0, memoryStr='0Mi', memory=0.0, memoryRatio=0.0, Disk=0.0, diskRatio=0.0}}
```
