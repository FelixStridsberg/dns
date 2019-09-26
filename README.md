# DNS
Experimental DNS server implementation in Kotlin.

## Proxy
This project includes a simple, single threaded, proxy that can be run to spy on the DNS traffic between a DNS client
and a DNS server.

### Usage

Compile
```
./gradlew build
```

Run the DNS server on 8080 with the real dns on 127.0.0.53 on port 53:
```
java -jar build/libs/dns-1.0-SNAPSHOT.jar 8080 127.0.0.53 53
```

Arguments
```
java -jar build/libs/dns-1.0-SNAPSHOT.jar SERVER_PORT FORWARDING_HOST FORWARDING_PORT
```

`FORWARDING_HOST` defaults to 127.0.0.53
`FORWARDING_PORT` defaults to 53


#### Example
Start proxy
```
java -jar build/libs/dns-1.0-SNAPSHOT.jar 8080 127.0.0.53 53
```

Run dig towards the proxy
```
dig @127.0.0.1 -p 8080 vadeen.com
```

Output from the proxy
```
>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
Got question:
Header (id: 40884, opcode: QUERY, qr: false, aa: false, tc: false, rc: true, ra: false, rcode: NOERR)
questions: 1, answers: 0, authorities: 0, additionals: 1

Questions:
vadeen.com                           IN         A

Additional:
                          0          U(4096)    U(41)      data_len=12

<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
Got answer:
Header (id: 40884, opcode: QUERY, qr: true, aa: false, tc: false, rc: true, ra: true, rcode: NOERR)
questions: 1, answers: 1, authorities: 0, additionals: 1

Questions:
vadeen.com                           IN         A

Answers:
vadeen.com                3228       IN         A          178.62.35.213

Additional:
                          0          U(65494)   U(41)      data_len=0
```

_Note: The Additional that looks weird is an OPT pseudo record defined in https://tools.ietf.org/html/rfc6891.
Currently not implemented in this implementation._