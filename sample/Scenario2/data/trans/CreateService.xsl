<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SP1-REG.CreateService.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:CreateService xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<Service_VALUE>
						<guid>
							<xsl:value-of
								select="$SP1-REG.CreateService.Req/soapenv:Envelope/soapenv:Body/q0:CreateService/args1" />
						</guid>
						<name>
							<xsl:value-of
								select="$SP1-REG.CreateService.Req/soapenv:Envelope/soapenv:Body/q0:CreateService/args2" />
						</name>
						<provider>
							<xsl:value-of
								select="$SP1-REG.CreateService.Req/soapenv:Envelope/soapenv:Body/q0:CreateService/args3" />
						</provider>
						<publicationTime>
							<xsl:value-of
								select="$SP1-REG.CreateService.Req/soapenv:Envelope/soapenv:Body/q0:CreateService/args4" />
						</publicationTime>
						<version>
							<xsl:value-of
								select="$SP1-REG.CreateService.Req/soapenv:Envelope/soapenv:Body/q0:CreateService/args5" />
						</version>
						<nature>
							<xsl:value-of
								select="$SP1-REG.CreateService.Req/soapenv:Envelope/soapenv:Body/q0:CreateService/args6" />
						</nature>
					</Service_VALUE>
				</q1:CreateService>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>