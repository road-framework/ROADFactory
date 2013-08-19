<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SettechnicalInterfaces.doneMsg" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
			<soapenv:Body>
				<q0:SettechnicalInterfacesResponse
					xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
					<xsl:value-of
						select="$SettechnicalInterfaces.doneMsg/soapenv:Envelope/soapenv:Body/q1:SettechnicalInterfacesResponse" />
				</q0:SettechnicalInterfacesResponse>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>