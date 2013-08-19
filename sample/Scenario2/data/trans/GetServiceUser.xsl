<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SC2-REG.GetServiceUser.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:GetService xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
					<Service_ID>
						<xsl:value-of
							select="$SC2-REG.GetServiceUser.Req/soapenv:Envelope/soapenv:Body/q0:GetServiceUser/args1" />
					</Service_ID>					
				</q1:GetService>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>