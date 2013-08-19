<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="Setcapabilities.doneMsg" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
			<soapenv:Body>
				<q0:SetcapabilitiesResponse
					xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
					<xsl:value-of
						select="$Setcapabilities.doneMsg/soapenv:Envelope/soapenv:Body/q1:SetcapabilitiesResponse" />
				</q0:SetcapabilitiesResponse>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>