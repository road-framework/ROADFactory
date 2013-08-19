<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="SP1-REG.CreateProvider.Req" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">

			<soapenv:Body>
				<q1:CreateProvider xmlns:q0="http://ws.apache.org/axis2"
					xmlns:q1="http://jaxws.core/">
					<Provider_VALUE>
						<guid>
							<xsl:value-of
								select="$SP1-REG.CreateProvider.Req/soapenv:Envelope/soapenv:Body/q0:CreateProvider/args1" />
						</guid>
					</Provider_VALUE>
				</q1:CreateProvider>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>