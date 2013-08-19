<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SP1-REG.Setcapabilities.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:Setcapabilities xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<Service_ROOTID>
						<xsl:value-of
							select="$SP1-REG.Setcapabilities.Req/soapenv:Envelope/soapenv:Body/q0:Setcapabilities/args1" />
					</Service_ROOTID>
					<Capability_ID>
						<xsl:value-of
							select="$SP1-REG.Setcapabilities.Req/soapenv:Envelope/soapenv:Body/q0:Setcapabilities/args2" />
					</Capability_ID>
				</q1:Setcapabilities>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>