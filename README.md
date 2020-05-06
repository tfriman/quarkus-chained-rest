# Simple REST API calling other APIs.

Stores input timestamps for service calls and collects them. Adapted from https://quarkus.io/guides/rest-client 

# Logic

Chains / calls to target url. Second to last in chain should call last's /timestamp.

# Building and running locally

```shell script
docker build -f src/main/docker/Dockerfile.native -t quarkus/chained-rest-caller .

docker run -i --rm -p 9001:8080 --name second \
       -e  target/mp-rest/url=http://second:8080/timestamp \
       quarkus/chained-rest-caller

docker run -i --rm -p 9000:8080 --name first \
       --link second \
       -e TARGET_MP_REST_URL=http://second:8080/timestamp \
       quarkus/chained-rest-caller
```
And then call 
```shell script
curl localhost:9000
```

# Pushing to Quay

```
docker login quay.io

docker tag quarkus/chained-rest-caller quay.io/tfriman/chained-rest-caller
docker push quay.io/tfriman/chained-rest-caller
```

# Usage with OpenShift
```shell script
oc new-app --name third quay.io/tfriman/chained-rest-caller -e "TARGET_MP_REST_URL=http://third:8080/timestamp"
oc new-app --name second quay.io/tfriman/chained-rest-caller -e "TARGET_MP_REST_URL=http://third:8080/timestamp"
oc new-app --name first  quay.io/tfriman/chained-rest-caller -e "TARGET_MP_REST_URL=http://second:8080/"

oc expose svc/first
oc patch route/first -p '{"spec":{"tls": {"insecureEdgeTerminationPolicy":"Allow"}}}'

curl https://$(oc get routes first --template '{{ .spec.host }}')/
```



