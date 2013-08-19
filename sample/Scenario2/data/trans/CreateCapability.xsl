<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SP1-REG.CreateCapability.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:CreateCapability xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<Capability_VALUE>
						<actions>
							<name><xsl:value-of
								select="$SP1-REG.CreateCapability.Req/soapenv:Envelope/soapenv:Body/q0:CreateCapability/args1" /></name>
						</actions>
						<name><xsl:value-of
								select="$SP1-REG.CreateCapability.Req/soapenv:Envelope/soapenv:Body/q0:CreateCapability/args2" /></name>
					</Capability_VALUE>
				</q1:CreateCapability>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>