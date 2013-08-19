<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SP1-REG.SettechnicalInterfaces.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:SettechnicalInterfaces xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<Service_ROOTID>
						<xsl:value-of
							select="$SP1-REG.SettechnicalInterfaces.Req/soapenv:Envelope/soapenv:Body/q0:SettechnicalInterfaces/args1" />
					</Service_ROOTID>
					<TechnicalInterface_ID>
						<xsl:value-of
							select="$SP1-REG.SettechnicalInterfaces.Req/soapenv:Envelope/soapenv:Body/q0:SettechnicalInterfaces/args2" />
					</TechnicalInterface_ID>
				</q1:SettechnicalInterfaces>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>