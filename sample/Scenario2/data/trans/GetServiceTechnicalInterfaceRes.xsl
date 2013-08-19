<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
	xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
	<xsl:output method="xml" indent="yes" />
	<xsl:param name="GetServiceTechnicalInterface.doneMsg" />
	<xsl:template match="/">
		<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/">
			<soapenv:Body>
				<q0:GetServiceTechnicalInterfaceResponse
					xmlns:q0="http://ws.apache.org/axis2" xmlns:q1="http://jaxws.core/">
					<return>
						<raasRef>
							<xsl:value-of
								select="$GetServiceTechnicalInterface.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/raasRef" />
						</raasRef>
						<technicalOperations>
							<name>
								<xsl:value-of
									select="$GetServiceTechnicalInterface.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/technicalOperations/name" />
							</name>
							<raasRef>
								<xsl:value-of
									select="$GetServiceTechnicalInterface.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/technicalOperations/raasRef" />
							</raasRef>
						</technicalOperations>
						<type>
							<xsl:value-of
								select="$GetServiceTechnicalInterface.doneMsg/soapenv:Envelope/soapenv:Body/q1:GetServiceResponse/return/technicalInterfaces/type" />
						</type>
					</return>
				</q0:GetServiceTechnicalInterfaceResponse>
			</soapenv:Body>
		</soapenv:Envelope>
	</xsl:template>
</xsl:stylesheet>